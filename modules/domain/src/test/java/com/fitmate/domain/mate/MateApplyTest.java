package com.fitmate.domain.mate;

import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.apply.MateApplyId;
import com.fitmate.domain.mate.enums.ApproveStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MateApply domain test")
class MateApplyTest {

    @Test
    @DisplayName("withoutId creates a new waiting apply")
    void withoutId() {
        MateApply apply = MateApply.withoutId("answer", 1L, 2L, ApproveStatus.WAIT, null);

        assertThat(apply.getId()).isNull();
        assertThat(apply.getComeAnswer()).isEqualTo("answer");
        assertThat(apply.getMateId()).isEqualTo(1L);
        assertThat(apply.getApplierId()).isEqualTo(2L);
        assertThat(apply.getApproveStatus()).isEqualTo(ApproveStatus.WAIT);
    }

    @Test
    @DisplayName("approve changes WAIT to APPROVE and returns true")
    void approve() {
        MateApply apply = MateApply.withoutId("answer", 1L, 2L, ApproveStatus.WAIT, null);

        boolean approved = apply.approve();

        assertThat(approved).isTrue();
        assertThat(apply.getApproveStatus()).isEqualTo(ApproveStatus.APPROVE);
    }

    @Test
    @DisplayName("approve returns false when already approved")
    void approveAlreadyApproved() {
        MateApply apply = MateApply.withoutId("answer", 1L, 2L, ApproveStatus.APPROVE, null);

        boolean approved = apply.approve();

        assertThat(approved).isFalse();
        assertThat(apply.getApproveStatus()).isEqualTo(ApproveStatus.APPROVE);
    }

    @Test
    @DisplayName("cancel sets reason and deletedAt")
    void cancel() {
        MateApply apply = MateApply.withId(
                new MateApplyId(1L), "answer", 1L, 2L,
                ApproveStatus.APPROVE, null,
                LocalDateTime.now(), LocalDateTime.now()
        );

        LocalDateTime now = LocalDateTime.now();
        apply.cancel("reason", now);

        assertThat(apply.getCancelReason()).isEqualTo("reason");
        assertThat(apply.getDeletedAt()).isEqualTo(now);
    }
}
