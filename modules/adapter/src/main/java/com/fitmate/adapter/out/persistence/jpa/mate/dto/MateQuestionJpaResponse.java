package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MateQuestionJpaResponse(
        Long profileImageId,
        String writerName,
        String comeQuestion
) {
    @QueryProjection
    public MateQuestionJpaResponse {
    }
}
