package com.fitmate.usecase.mate.event.dto;

import com.fitmate.domain.mate.enums.ApproveStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateRequestEventDto {
    private final String title;
    private final Long mateId;
    private final Long writerId;
    private final Long applierId;
    private final ApproveStatus approveStatus;
}
