package com.fitmate.adapter.integration;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[통합] 메이트 신청/승인 플로우")
class MateApplyFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired private AccountProfileUseCasePort accountUseCase;
    @Autowired private MateUseCasePort mateUseCase;
    @Autowired private MateApplyUseCasePort mateApplyUseCase;
    @Autowired private AccountRepository accountRepository;

    private Long writerId;
    private Long applierId;
    private Long mateId;

    @BeforeEach
    void setUp() throws Exception {
        // 작성자 가입
        accountUseCase.join(new AccountJoinCommand(
                "writer2", "!Passw0rd!", "작성자2", "소개",
                "박작성", "01099990002", "writer2@test.com",
                LocalDate.of(1990, 1, 1), AccountRole.USER, Gender.MALE
        ));
        writerId = accountRepository.getByLoginName("writer2").getId();

        // 신청자 가입
        accountUseCase.join(new AccountJoinCommand(
                "applier2", "!Passw0rd!", "신청자2", "소개",
                "김신청", "01099990003", "applier2@test.com",
                LocalDate.of(2000, 5, 10), AccountRole.USER, Gender.MALE
        ));
        applierId = accountRepository.getByLoginName("applier2").getId();

        // 메이트 등록 (승인제, 3명 모집)
        mateUseCase.registerMate(new MateCreateCommand(
                FitCategory.FITNESS, "신청 테스트 메이트", "같이 운동", null,
                LocalDateTime.now().plusDays(7),
                "험블짐", "서울시 용산구",
                GatherType.AGREE, PermitGender.ALL,
                50, 15, 3,
                List.of(MateFee.withOutId(null, "장소비", 5000)),
                "어떤 운동?",
                writerId
        ));
        mateId = mateUseCase.findMyMates(writerId).get(0).getId();
    }

    @Nested
    @DisplayName("메이트 신청")
    class Apply {

        @Test
        @DisplayName("승인제 메이트 신청 → 대기(WAIT) 상태로 저장")
        void applyToAgreeMate() {
            mateApplyUseCase.applyMate(new MateApplyCommand(mateId, applierId, "스쿼트 위주로 합니다"));

            MateDetailResponse detail = mateUseCase.findMate(mateId);
            assertThat(detail.getWaitingAccountIds()).contains(applierId);
            assertThat(detail.getApprovedAccountIds()).doesNotContain(applierId);
        }

        @Test
        @DisplayName("작성자 본인이 신청하면 NotMatchException")
        void writerCannotApply() {
            assertThatThrownBy(() ->
                    mateApplyUseCase.applyMate(new MateApplyCommand(mateId, writerId, "답변"))
            ).isInstanceOf(NotMatchException.class);
        }
    }

    @Nested
    @DisplayName("메이트 승인")
    class Approve {

        @Test
        @DisplayName("신청 → 승인 → 상세 조회에서 승인 확인")
        void applyAndApprove() {
            // 신청
            mateApplyUseCase.applyMate(new MateApplyCommand(mateId, applierId, "답변"));

            // 승인
            mateApplyUseCase.approveMate(new MateApproveCommand(mateId, applierId, writerId));

            // 확인
            MateDetailResponse detail = mateUseCase.findMate(mateId);
            assertThat(detail.getApprovedAccountIds()).contains(applierId, writerId);
            assertThat(detail.getWaitingAccountIds()).doesNotContain(applierId);
        }

        @Test
        @DisplayName("작성자가 아닌 유저가 승인하면 NotMatchException")
        void nonWriterCannotApprove() {
            mateApplyUseCase.applyMate(new MateApplyCommand(mateId, applierId, "답변"));

            assertThatThrownBy(() ->
                    mateApplyUseCase.approveMate(new MateApproveCommand(mateId, applierId, applierId))
            ).isInstanceOf(NotMatchException.class);
        }
    }

    @Nested
    @DisplayName("메이트 신청 취소")
    class Cancel {

        @Test
        @DisplayName("승인 후 취소 → approvedCount 감소")
        void cancelAfterApprove() {
            mateApplyUseCase.applyMate(new MateApplyCommand(mateId, applierId, "답변"));
            mateApplyUseCase.approveMate(new MateApproveCommand(mateId, applierId, writerId));

            // 취소
            mateApplyUseCase.cancelMateApply(mateId, applierId, "개인 사정");

            MateDetailResponse detail = mateUseCase.findMate(mateId);
            assertThat(detail.getApprovedAccountIds()).doesNotContain(applierId);
        }
    }

    @Nested
    @DisplayName("마감된 메이트 신청")
    class ApplyToClosedMate {

        @Test
        @DisplayName("마감된 메이트에 신청하면 LimitException")
        void cannotApplyToClosedMate() {
            mateUseCase.closeMate(mateId, writerId);

            assertThatThrownBy(() ->
                    mateApplyUseCase.applyMate(new MateApplyCommand(mateId, applierId, "답변"))
            ).isInstanceOf(LimitException.class);
        }
    }
}
