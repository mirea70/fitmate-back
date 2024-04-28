package com.fitmate.domain.mate.apply;

import com.fitmate.domain.mate.enums.ApproveStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MateApply {

    private final MateApplyId id;

    private final String comeAnswer;

    private final Long mateId;

    private final Long applierId;

    private ApproveStatus approveStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static MateApply withId(MateApplyId id, String comeAnswer, Long mateId, Long applierId, ApproveStatus approveStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new MateApply(
                id,
                comeAnswer,
                mateId,
                applierId,
                approveStatus,
                createdAt,
                updatedAt,
                null);
    }

    public static MateApply withoutId(String comeAnswer, Long mateId, Long applierId, ApproveStatus approveStatus) {
        return new MateApply(
                null,
                comeAnswer,
                mateId,
                applierId,
                approveStatus,
                null,
                null,
                null);
    }

    public void changeToApprove() {
        this.approveStatus = ApproveStatus.APPROVE;
    }
}
