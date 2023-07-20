package com.fitmate.domain.mating.mate.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

public class MatingQuestionDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    public static class Response {
        private Long profileImageId;

        private String writerName;

        private String comeQuestion;

        @QueryProjection
        public Response(Long profileImageId, String writerName, String comeQuestion) {
            this.profileImageId = profileImageId;
            this.writerName = writerName;
            this.comeQuestion = comeQuestion;
        }
    }
}
