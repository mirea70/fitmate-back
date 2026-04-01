package com.fitmate.usecase.mate.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateRegisteredEventDto {
    private final String title;
    private final Long mateId;
    private final Long writerId;
}
