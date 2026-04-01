package com.fitmate.usecase.mate.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateCancelledEventDto {
    private final Long mateId;
    private final Long applierId;
    private final boolean wasApproved;
}
