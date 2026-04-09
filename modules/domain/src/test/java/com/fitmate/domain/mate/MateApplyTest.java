package com.fitmate.domain.mate;

import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.apply.MateApplyId;
import com.fitmate.domain.mate.enums.ApproveStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MateApply 도메인 테스트")
class MateApplyTest {

    @Test
    @DisplayName("withoutId로 생성하면 id, createdAt, updatedAt이 null")
    void withoutId() {
        MateApply apply = MateApply.withoutId("답변", 1L, 2L, ApproveStatus.WAIT, null);

        assertThat(apply.getId()).isNull();
        assertThat(apply.getComeAnswer()).isEqualTo("답변");
        assertThat(apply.getMateId()).isEqualTo(1L);
        assertThat(apply.getApplierId()).isEqualTo(2L);
        assertThat(apply.getApproveStatus()).isEqualTo(ApproveStatus.WAIT);
    }

    @Test
    @DisplayName("changeToApprove — 대기 상태를 승인으로 변경")
    void changeToApprove() {
        MateApply apply = MateApply.withoutId("답변", 1L, 2L, ApproveStatus.WAIT, null);
        apply.changeToApprove();
        assertThat(apply.getApproveStatus()).isEqualTo(ApproveStatus.APPROVE);
    }

    @Test
    @DisplayName("cancel — 취소 사유와 삭제 시간이 설정됨")
    void cancel() {
        MateApply apply = MateApply.withId(
                new MateApplyId(1L), "답변", 1L, 2L,
                ApproveStatus.APPROVE, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        LocalDateTime now = LocalDateTime.now();
        apply.cancel("개인 사정", now);

        assertThat(apply.getCancelReason()).isEqualTo("개인 사정");
        assertThat(apply.getDeletedAt()).isEqualTo(now);
    }
}
