package com.fitmate.usecase.mate;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.account.Password;
import com.fitmate.domain.account.PrivateInfo;
import com.fitmate.domain.account.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.mate.FitPlace;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateId;
import com.fitmate.domain.mate.PermitAges;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.file.LoadAttachFilePort;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.usecase.mate.mapper.MateUseCaseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("MateUseCase 단위 테스트")
class MateUseCaseTest {

    @InjectMocks
    private MateUseCase mateUseCase;

    @Mock private LoadMatePort loadMatePort;
    @Mock private LoadMateRequestPort loadMateRequestPort;
    @Mock private LoadAccountPort loadAccountPort;
    @Mock private LoadAttachFilePort loadAttachFilePort;
    @Mock private LoadMateWishPort loadMateWishPort;
    @Mock private MateUseCaseMapper mateUseCaseMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    private static final Long WRITER_ID = 1L;
    private static final Long MATE_ID = 10L;

    private Mate createMate(int approvedCount) {
        return Mate.withId(
                new MateId(MATE_ID), FitCategory.FITNESS, "제목", "소개", null,
                LocalDateTime.now().plusDays(7),
                new FitPlace("짐", "서울"), GatherType.AGREE, PermitGender.ALL,
                new PermitAges(50, 15), 5, WRITER_ID,
                Collections.emptyList(), "질문",
                approvedCount, null, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    private Account createWriter() {
        return Account.withId(
                new AccountId(WRITER_ID), "writer", new Password("pw"),
                new ProfileInfo("작성자", "소개", 1L),
                new PrivateInfo("홍길동", "010", "w@w.com", LocalDate.of(1995, 1, 1)),
                Gender.MALE, AccountRole.USER,
                LocalDateTime.now(), LocalDateTime.now(), null, null, null
        );
    }

    @Nested
    @DisplayName("findMate")
    class FindMate {

        @Test
        @DisplayName("메이트 상세 조회 — 작성자가 approvedAccountIds에 포함")
        void findMate() {
            Mate mate = createMate(1);
            Account writer = createWriter();
            given(loadMatePort.loadMateEntity(any(MateId.class))).willReturn(mate);
            given(loadAccountPort.loadAccountEntity(any(AccountId.class))).willReturn(writer);
            given(loadMateRequestPort.getWaitingAccountIds(MATE_ID)).willReturn(new HashSet<>());
            given(loadMateRequestPort.getApprovedAccountIds(MATE_ID)).willReturn(new HashSet<>(Set.of(2L)));
            given(mateUseCaseMapper.domainToDetailResponse(any(), any(), any(), any()))
                    .willAnswer(inv -> {
                        Set<Long> approved = inv.getArgument(3);
                        assertThat(approved).contains(WRITER_ID, 2L);
                        return null;
                    });

            mateUseCase.findMate(MATE_ID);

            then(mateUseCaseMapper).should().domainToDetailResponse(any(), any(), any(), any());
        }
    }

    @Nested
    @DisplayName("closeMate")
    class CloseMate {

        @Test
        @DisplayName("작성자가 아닌 유저가 마감하면 NotMatchException")
        void notWriter() {
            Mate mate = createMate(0);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            assertThatThrownBy(() -> mateUseCase.closeMate(MATE_ID, 999L))
                    .isInstanceOf(NotMatchException.class);
        }

        @Test
        @DisplayName("정상 마감 — 대기자 자동 취소 + 찜한 유저에게 알림")
        void closeSuccess() {
            Mate mate = createMate(0);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);
            given(loadMateRequestPort.getWaitingAccountIds(MATE_ID)).willReturn(new HashSet<>());
            given(loadMateWishPort.getWisherAccountIds(MATE_ID)).willReturn(Collections.emptyList());

            mateUseCase.closeMate(MATE_ID, WRITER_ID);

            assertThat(mate.isClosed()).isTrue();
        }
    }
}
