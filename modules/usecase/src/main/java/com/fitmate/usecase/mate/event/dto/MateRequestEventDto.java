package com.fitmate.usecase.mate.event.dto;

import com.fitmate.domain.mate.enums.ApproveStatus;

public record MateRequestEventDto(
        String title,
        Long mateId,
        Long writerId,
        Long applierId,
        ApproveStatus approveStatus
) {
}
