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
import com.fitmate.port.out.account.AccountProfileResponse;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[통합] 메이트 등록/조회 플로우")
class MateFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AccountProfileUseCasePort accountUseCase;

    @Autowired
    private MateUseCasePort mateUseCase;

    @Autowired
    private AccountRepository accountRepository;

    private Long writerId;

    @BeforeEach
    void setUp() throws Exception {
        accountUseCase.join(new AccountJoinCommand(
                "writer1", "!Passw0rd!", "작성자", "소개",
                "박작성", "01099990001", "writer1@test.com",
                LocalDate.of(1995, 3, 15), AccountRole.USER, Gender.MALE
        ));
        writerId = accountRepository.getByLoginName("writer1").getId();
    }

    private void registerMate(String title, GatherType gatherType) {
        mateUseCase.registerMate(new MateCreateCommand(
                FitCategory.FITNESS, title, "같이 운동해요", null,
                LocalDateTime.now().plusDays(7),
                "험블짐", "서울시 용산구",
                gatherType, PermitGender.ALL,
                50, 15, 5,
                List.of(MateFee.withOutId(null, "장소비", 10000)),
                "어떤 운동 좋아하세요?",
                writerId
        ));
    }

    @Nested
    @DisplayName("메이트 등록 + 조회")
    class RegisterAndFind {

        @Test
        @DisplayName("메이트 등록 후 내 메이트 목록에서 확인")
        void registerAndFindMyMates() {
            registerMate("운동 메이트 구함", GatherType.AGREE);

            List<MateSimpleResponse> myMates = mateUseCase.findMyMates(writerId);
            assertThat(myMates).hasSize(1);
            assertThat(myMates.get(0).getTitle()).isEqualTo("운동 메이트 구함");
        }

        @Test
        @DisplayName("메이트 상세 조회 — 작성자가 approvedAccountIds에 포함")
        void findMateDetail() {
            registerMate("상세 조회 테스트", GatherType.AGREE);

            Long mateId = mateUseCase.findMyMates(writerId).get(0).getId();
            MateDetailResponse detail = mateUseCase.findMate(mateId);

            assertThat(detail.getTitle()).isEqualTo("상세 조회 테스트");
            assertThat(detail.getWriterAccountId()).isEqualTo(writerId);
            assertThat(detail.getApprovedAccountIds()).contains(writerId);
            assertThat(detail.isClosed()).isFalse();
            assertThat(detail.getTotalFee()).isEqualTo(10000);
        }
    }

    @Nested
    @DisplayName("메이트 마감")
    class CloseMate {

        @Test
        @DisplayName("마감 후 상세 조회에서 closed=true")
        void closeAndVerify() {
            registerMate("마감 테스트", GatherType.AGREE);
            Long mateId = mateUseCase.findMyMates(writerId).get(0).getId();

            mateUseCase.closeMate(mateId, writerId);

            MateDetailResponse detail = mateUseCase.findMate(mateId);
            assertThat(detail.isClosed()).isTrue();
        }
    }
}
