package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MateQuestionJpaResponse {
    private final Long profileImageId;
    private final String writerName;
    private final String comeQuestion;

    @QueryProjection
    public MateQuestionJpaResponse(Long profileImageId, String writerName, String comeQuestion) {
        this.profileImageId = profileImageId;
        this.writerName = writerName;
        this.comeQuestion = comeQuestion;
    }
}
