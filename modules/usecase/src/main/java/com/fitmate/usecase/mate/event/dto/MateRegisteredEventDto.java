package com.fitmate.usecase.mate.event.dto;

public record MateRegisteredEventDto(
        String title,
        Long mateId,
        Long writerId
) {
}
