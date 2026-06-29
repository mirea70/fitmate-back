package com.fitmate.adapter.in.web.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public record MateApproveRequest(
        @Schema(description = "승인할 회원 식별 ID", example = "1")
        @NotNull(message = "신청자 식별 ID값은 필수입니다.")
        Long applierId
) {
}
