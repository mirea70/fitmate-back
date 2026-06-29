package com.fitmate.usecase.mate.event.dto;

public record MateCancelledEventDto(
        String title,
        Long mateId,
        Long writerId,
        Long applierId,
        String cancelReason,
        boolean wasApproved
) {
}
