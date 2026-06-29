package com.fitmate.adapter.in.web.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(description = "카카오 회원가입 요청 DTO")
public record KakaoRegisterRequest(
        @NotBlank(message = "카카오 액세스 토큰은 필수입니다.")
        @Schema(description = "카카오 액세스 토큰")
        String accessToken,

        @NotBlank(message = "이름은 필수입니다.")
        @Schema(description = "이름", example = "홍길동")
        String name,

        @NotBlank(message = "성별은 필수입니다.")
        @Schema(description = "성별: MALE 또는 FEMALE", example = "MALE")
        String gender,

        @NotBlank(message = "생년월일은 필수입니다.")
        @Schema(description = "생년월일", example = "1995-03-15")
        String birthDate,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Schema(description = "전화번호", example = "01012345678")
        String phone,

        @NotBlank(message = "닉네임은 필수입니다.")
        @Schema(description = "닉네임", example = "홍시")
        String nickName,

        @Schema(description = "이메일", example = "abc@naver.com")
        String email
) {
}
