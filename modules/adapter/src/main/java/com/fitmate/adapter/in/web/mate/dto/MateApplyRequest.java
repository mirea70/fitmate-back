package com.fitmate.adapter.in.web.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "메이트 신청 DTO")
public record MateApplyRequest(
        @Schema(description = "메이트 신청 질문에 대한 답변", example = "스쿼트 위주로 해요")
        @NotNull(message = "신청질문 답변 입력은 필수입니다.")
        String comeAnswer
) {
}
