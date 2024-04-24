package com.fitmate.port.out.mate.dto;

import lombok.Getter;

@Getter
public class MateQuestionResponse {
    private final Long profileImageId;
    private final String writerName;
    private final String comeQuestion;

    public MateQuestionResponse(Long profileImageId, String writerName, String comeQuestion) {
        this.profileImageId = profileImageId;
        this.writerName = writerName;
        this.comeQuestion = comeQuestion;
    }
}
