package com.fitmate.adapter.in.web.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Schema(description = "메이트 신청 DTO")
public class MateApplyRequest {
    @Schema(description = "메이트 신청 질문에 대한 답변", example = "스쿼트 위주로 해요")
    @NotNull(message = "신청질문 답변 입력은 필수입니다.")
    private String comeAnswer;

    public MateApplyRequest(String comeAnswer) {
        this.comeAnswer = comeAnswer;
    }
}
