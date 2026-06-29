package com.fitmate.port.out.mate.dto;

public record MateQuestionResponse(
        Long profileImageId,
        String writerName,
        String comeQuestion
) {
}
