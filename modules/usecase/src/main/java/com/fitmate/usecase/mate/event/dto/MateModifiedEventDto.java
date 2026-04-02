package com.fitmate.usecase.mate.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateModifiedEventDto {
    private final String title;
    private final Long mateId;
}
