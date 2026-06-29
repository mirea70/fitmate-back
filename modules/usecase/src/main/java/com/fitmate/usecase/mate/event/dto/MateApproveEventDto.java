package com.fitmate.usecase.mate.event.dto;

public record MateApproveEventDto(
        String title,
        Long mateId,
        Long applierId
) {
}
