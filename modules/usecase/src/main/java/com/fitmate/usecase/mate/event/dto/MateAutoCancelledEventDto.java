package com.fitmate.usecase.mate.event.dto;

public record MateAutoCancelledEventDto(
        String title,
        Long mateId,
        Long writerId,
        Long applierId,
        String cancelReason
) {
}
