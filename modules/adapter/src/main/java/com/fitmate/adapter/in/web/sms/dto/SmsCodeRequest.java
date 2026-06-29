package com.fitmate.adapter.in.web.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "인증번호 요청 Model")
public record SmsCodeRequest(
        @Schema(description = "휴대폰번호", example = "01013245673")
        @NotNull
        @Pattern(regexp = "^010\\d{4}\\d{4}$")
        String phone
) {
}
