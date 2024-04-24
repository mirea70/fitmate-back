package com.fitmate.domain.mate.aggregate;

import com.fitmate.domain.mate.vo.ApproveStatus;
import com.fitmate.domain.mate.vo.MateRequestId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MateRequest {

    private final MateRequestId id;

    private final String comeAnswer;

    private final Long mateId;

    private final Long applierId;

    private ApproveStatus approveStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static MateRequest withId(MateRequestId id, String comeAnswer, Long mateId, Long applierId, ApproveStatus approveStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new MateRequest(
                id,
                comeAnswer,
                mateId,
                applierId,
                approveStatus,
                createdAt,
                updatedAt,
                null);
    }

    public static MateRequest withoutId(String comeAnswer, Long mateId, Long applierId, ApproveStatus approveStatus) {
        return new MateRequest(
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
