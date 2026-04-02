package com.fitmate.usecase.mate.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateAutoCancelledEventDto {
    private final String title;
    private final Long mateId;
    private final Long writerId;
    private final Long applierId;
    private final String cancelReason;
}
