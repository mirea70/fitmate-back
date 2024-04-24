package com.fitmate.usecase.mate.event.dto;

import lombok.Getter;

@Getter
public class MateRequestEventDto {
    private final String title;
    private final Long mateId;
    private final Long applierId;

    public MateRequestEventDto(String title, Long mateId, Long applierId) {
        this.title = title;
        this.mateId = mateId;
        this.applierId = applierId;
    }
}
