package com.fitmate.app.mate.mating.dto;

import lombok.*;

public class MateEventDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private Long matingId;

        private Long accountId;
    }
}
