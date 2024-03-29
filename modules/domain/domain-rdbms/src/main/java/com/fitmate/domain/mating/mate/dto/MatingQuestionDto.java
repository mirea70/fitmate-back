package com.fitmate.domain.mating.mate.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class MatingQuestionDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @Schema(description = "메이트 신청 질문 조회 응답 DTO")
    public static class Response {

        @Schema(description = "작성자 프로필 이미지 식별 ID", example = "3")
        private Long profileImageId;

        @Schema(description = "작성자 닉네임", example = "만두")
        private String writerName;

        @Schema(description = "메이트 신청 질문", example = "3대 중량이 어떻게 되세요?")
        private String comeQuestion;

        @QueryProjection
        public Response(Long profileImageId, String writerName, String comeQuestion) {
            this.profileImageId = profileImageId;
            this.writerName = writerName;
            this.comeQuestion = comeQuestion;
        }
    }
}
