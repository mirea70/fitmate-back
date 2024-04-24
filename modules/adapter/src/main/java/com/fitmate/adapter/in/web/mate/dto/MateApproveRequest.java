package com.fitmate.adapter.in.web.mate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MateApproveRequest {
    @Schema(description = "승인할 회원 식별 ID", example = "1")
    @NotNull(message = "신청자 식별 ID값은 필수입니다.")
    private Long applierId;

    public MateApproveRequest(Long applierId) {
        this.applierId = applierId;
    }
}
