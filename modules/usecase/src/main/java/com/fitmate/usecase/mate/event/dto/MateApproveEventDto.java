package com.fitmate.usecase.mate.event.dto;

import lombok.Getter;

@Getter
public class MateApproveEventDto {
    private final String title;
    private final Long mateId;
    private final Long applierId;

    public MateApproveEventDto(String title, Long mateId, Long applierId) {
        this.title = title;
        this.mateId = mateId;
        this.applierId = applierId;
    }
}
