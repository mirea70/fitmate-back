package com.fitmate.app.mate.mating.dto;

import lombok.*;

import java.util.Set;

public class MateEventDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String title;

        private Long matingId;

        private Long accountId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Approve {

        private String title;

        private Long matingId;

        private Set<Long> accountIds;
    }
}
