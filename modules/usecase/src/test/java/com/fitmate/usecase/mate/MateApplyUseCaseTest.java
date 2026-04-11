package com.fitmate.usecase.mate;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.account.Password;
import com.fitmate.domain.account.PrivateInfo;
import com.fitmate.domain.account.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.mate.FitPlace;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateId;
import com.fitmate.domain.mate.PermitAges;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.apply.MateApplyId;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import com.fitmate.usecase.mate.mapper.MateUseCaseMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("MateApplyUseCase 단위 테스트")
class MateApplyUseCaseTest {

    @InjectMocks
    private MateApplyUseCase mateApplyUseCase;

    @Mock private LoadMatePort loadMatePort;
    @Mock private LoadMateRequestPort loadMateRequestPort;
    @Mock private LoadAccountPort loadAccountPort;
    @Mock private MateUseCaseMapper mateUseCaseMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    private static final Long WRITER_ID = 1L;
    private static final Long APPLIER_ID = 2L;
    private static final Long MATE_ID = 10L;

    private Mate createMate(GatherType gatherType, int approvedCount, int permitPeopleCnt, PermitGender permitGender) {
        return Mate.withId(
                new MateId(MATE_ID), FitCategory.FITNESS, "제목", "소개", null,
                LocalDateTime.now().plusDays(7),
                new FitPlace("짐", "서울"), gatherType, permitGender,
                new PermitAges(50, 15), permitPeopleCnt, WRITER_ID,
                Collections.emptyList(), "질문",
                approvedCount, null, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    private Account createAccount(Long id, Gender gender, LocalDate birthDate) {
        return Account.withId(
                new AccountId(id), "user" + id, new Password("pw"),
                new ProfileInfo("닉" + id, null, null),
                new PrivateInfo("이름", "010", "e@e.com", birthDate),
                gender, AccountRole.USER,
                LocalDateTime.now(), LocalDateTime.now(), null, null, null
        );
    }

    @Test
    @DisplayName("readQuestion — 질문 조회 위임")
    void readQuestion() {
        MateQuestionResponse response = new MateQuestionResponse(1L, "작성자", "질문입니다");
        given(loadMateRequestPort.loadMateQuestion(MATE_ID)).willReturn(response);

        mateApplyUseCase.readQuestion(MATE_ID);

        then(loadMateRequestPort).should().loadMateQuestion(MATE_ID);
    }

    @Nested
    @DisplayName("applyMate")
    class ApplyMate {

        @Test
        @DisplayName("작성자가 본인 글에 신청하면 NotMatchException")
        void writerCannotApply() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            MateApplyCommand command = new MateApplyCommand(MATE_ID, WRITER_ID, "답변");

            assertThatThrownBy(() -> mateApplyUseCase.applyMate(command))
                    .isInstanceOf(NotMatchException.class);
        }

        @Test
        @DisplayName("마감된 메이트에 신청하면 LimitException")
        void closedMate() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.ALL);
            mate.close();
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            Account applier = createAccount(APPLIER_ID, Gender.MALE, LocalDate.of(2000, 1, 1));
            given(loadAccountPort.loadAccountEntity(any(AccountId.class))).willReturn(applier);

            MateApplyCommand command = new MateApplyCommand(MATE_ID, APPLIER_ID, "답변");

            assertThatThrownBy(() -> mateApplyUseCase.applyMate(command))
                    .isInstanceOf(LimitException.class);
        }

        @Test
        @DisplayName("성별 조건 불일치 시 NotMatchException")
        void genderMismatch() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.MALE);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            Account femaleApplier = createAccount(APPLIER_ID, Gender.FEMALE, LocalDate.of(2000, 1, 1));
            given(loadAccountPort.loadAccountEntity(any(AccountId.class))).willReturn(femaleApplier);

            MateApplyCommand command = new MateApplyCommand(MATE_ID, APPLIER_ID, "답변");

            assertThatThrownBy(() -> mateApplyUseCase.applyMate(command))
                    .isInstanceOf(NotMatchException.class);
        }

        @Test
        @DisplayName("FAST 모집이면 즉시 APPROVE + incrementApprovedCount 호출")
        void fastGatherAutoApproves() {
            Mate mate = createMate(GatherType.FAST, 0, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            Account applier = createAccount(APPLIER_ID, Gender.MALE, LocalDate.of(2000, 1, 1));
            given(loadAccountPort.loadAccountEntity(any(AccountId.class))).willReturn(applier);

            MateApply mateApply = MateApply.withoutId("답변", MATE_ID, APPLIER_ID, ApproveStatus.APPROVE, null);
            given(mateUseCaseMapper.commandToDomain(any(MateApplyCommand.class), eq(ApproveStatus.APPROVE))).willReturn(mateApply);

            MateApplyCommand command = new MateApplyCommand(MATE_ID, APPLIER_ID, "답변");
            mateApplyUseCase.applyMate(command);

            then(loadMatePort).should().incrementApprovedCount(any(MateId.class));
        }

        @Test
        @DisplayName("AGREE 모집이면 WAIT 상태로 저장, incrementApprovedCount 호출 안됨")
        void agreeGatherWaits() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            Account applier = createAccount(APPLIER_ID, Gender.MALE, LocalDate.of(2000, 1, 1));
            given(loadAccountPort.loadAccountEntity(any(AccountId.class))).willReturn(applier);

            MateApply mateApply = MateApply.withoutId("답변", MATE_ID, APPLIER_ID, ApproveStatus.WAIT, null);
            given(mateUseCaseMapper.commandToDomain(any(MateApplyCommand.class), eq(ApproveStatus.WAIT))).willReturn(mateApply);

            MateApplyCommand command = new MateApplyCommand(MATE_ID, APPLIER_ID, "답변");
            mateApplyUseCase.applyMate(command);

            then(loadMatePort).should(never()).incrementApprovedCount(any(MateId.class));
        }
    }

    @Nested
    @DisplayName("approveMate")
    class ApproveMate {

        @Test
        @DisplayName("작성자가 아닌 사용자가 승인하면 NotMatchException")
        void notWriterThrows() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            MateApproveCommand command = new MateApproveCommand(MATE_ID, APPLIER_ID, 999L); // 999L != WRITER_ID

            assertThatThrownBy(() -> mateApplyUseCase.approveMate(command))
                    .isInstanceOf(NotMatchException.class);
        }

        @Test
        @DisplayName("정상 승인 — MateApply APPROVE + incrementApprovedCount 호출")
        void approveSuccess() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            MateApply apply = MateApply.withId(
                    new MateApplyId(1L), "답변", MATE_ID, APPLIER_ID,
                    ApproveStatus.WAIT, null, LocalDateTime.now(), LocalDateTime.now()
            );
            Loaded<MateApply> loadedApply = new Loaded<>(apply, a -> {});
            given(loadMateRequestPort.loadMateApply(MATE_ID, APPLIER_ID)).willReturn(loadedApply);

            MateApproveCommand command = new MateApproveCommand(MATE_ID, APPLIER_ID, WRITER_ID);
            mateApplyUseCase.approveMate(command);

            org.assertj.core.api.Assertions.assertThat(apply.getApproveStatus()).isEqualTo(ApproveStatus.APPROVE);
            then(loadMatePort).should().incrementApprovedCount(any(MateId.class));
        }
    }

    @Nested
    @DisplayName("cancelMateApply")
    class CancelMateApply {

        @Test
        @DisplayName("승인 상태 취소 시 decrementApprovedCount 호출")
        void cancelApproved() {
            Mate mate = createMate(GatherType.FAST, 1, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            MateApply apply = MateApply.withId(
                    new MateApplyId(1L), "답변", MATE_ID, APPLIER_ID,
                    ApproveStatus.APPROVE, null, LocalDateTime.now(), LocalDateTime.now()
            );
            Loaded<MateApply> loadedApply = new Loaded<>(apply, a -> {});
            given(loadMateRequestPort.loadMateApply(MATE_ID, APPLIER_ID)).willReturn(loadedApply);

            mateApplyUseCase.cancelMateApply(MATE_ID, APPLIER_ID, "개인 사정");

            then(loadMatePort).should().decrementApprovedCount(any(MateId.class));
            org.assertj.core.api.Assertions.assertThat(apply.getCancelReason()).isEqualTo("개인 사정");
        }

        @Test
        @DisplayName("대기 상태 취소 시 decrementApprovedCount 호출 안됨")
        void cancelWaiting() {
            Mate mate = createMate(GatherType.AGREE, 0, 5, PermitGender.ALL);
            Loaded<Mate> loadedMate = new Loaded<>(mate, m -> {});
            given(loadMatePort.loadMate(any(MateId.class))).willReturn(loadedMate);

            MateApply apply = MateApply.withId(
                    new MateApplyId(1L), "답변", MATE_ID, APPLIER_ID,
                    ApproveStatus.WAIT, null, LocalDateTime.now(), LocalDateTime.now()
            );
            Loaded<MateApply> loadedApply = new Loaded<>(apply, a -> {});
            given(loadMateRequestPort.loadMateApply(MATE_ID, APPLIER_ID)).willReturn(loadedApply);

            mateApplyUseCase.cancelMateApply(MATE_ID, APPLIER_ID, "변심");

            then(loadMatePort).should(never()).decrementApprovedCount(any(MateId.class));
        }
    }
}
