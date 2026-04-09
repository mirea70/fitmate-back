package com.fitmate.adapter.integration;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
import com.fitmate.port.in.mate.usecase.MateWishUseCasePort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[통합] 메이트 찜 플로우")
class MateWishFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired private AccountProfileUseCasePort accountUseCase;
    @Autowired private MateUseCasePort mateUseCase;
    @Autowired private MateWishUseCasePort mateWishUseCase;
    @Autowired private AccountRepository accountRepository;

    private Long writerId;
    private Long userId;
    private Long mateId;

    @BeforeEach
    void setUp() throws Exception {
        accountUseCase.join(new AccountJoinCommand(
                "writer3", "!Passw0rd!", "작성자3", "소개",
                "박작성", "01099990004", "writer3@test.com",
                LocalDate.of(1995, 1, 1), AccountRole.USER, Gender.MALE
        ));
        writerId = accountRepository.getByLoginName("writer3").getId();

        accountUseCase.join(new AccountJoinCommand(
                "wisher3", "!Passw0rd!", "찜러3", "소개",
                "이찜러", "01099990005", "wisher3@test.com",
                LocalDate.of(2000, 1, 1), AccountRole.USER, Gender.FEMALE
        ));
        userId = accountRepository.getByLoginName("wisher3").getId();

        mateUseCase.registerMate(new MateCreateCommand(
                FitCategory.YOGA, "요가 메이트", "같이 요가해요", null,
                LocalDateTime.now().plusDays(7),
                "요가원", "서울시 강남구",
                GatherType.FAST, PermitGender.ALL,
                50, 15, 5,
                List.of(MateFee.withOutId(null, "수업비", 15000)),
                "요가 경험 있나요?",
                writerId
        ));
        mateId = mateUseCase.findMyMates(writerId).get(0).getId();
    }

    @Test
    @DisplayName("찜 추가 → true 반환")
    void addWish() {
        boolean result = mateWishUseCase.toggleWish(userId, mateId);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("찜 추가 후 내 찜 목록에서 확인")
    void wishAppearsInMyList() {
        mateWishUseCase.toggleWish(userId, mateId);

        List<MateSimpleResponse> wishes = mateWishUseCase.getMyWishMates(userId);
        assertThat(wishes).hasSize(1);
        assertThat(wishes.get(0).getTitle()).isEqualTo("요가 메이트");
    }

    @Test
    @DisplayName("찜 토글 (추가 → 삭제) → false 반환 + 목록에서 제거")
    void toggleWishRemoves() {
        mateWishUseCase.toggleWish(userId, mateId);  // 추가
        boolean result = mateWishUseCase.toggleWish(userId, mateId);  // 삭제

        assertThat(result).isFalse();
        assertThat(mateWishUseCase.getMyWishMates(userId)).isEmpty();
    }
}
