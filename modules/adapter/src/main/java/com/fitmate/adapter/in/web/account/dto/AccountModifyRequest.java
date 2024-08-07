package com.fitmate.adapter.in.web.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Schema(description = "회원가입 요청 Model")
public class AccountModifyRequest {

    @Schema(description = "유저 별칭 (중복 불가)", example = "홍시")
    @Size(min = 2, max = 10)
    @NotNull
    @Pattern(regexp = "^[a-zA-Zㄱ-힣\\d|s]*$")
    private String nickName;

    @Schema(description = "자기소개", example = "안녕하세요 누구입니다.")
    @Size(max = 50)
    private String introduction;

    @Schema(description = "성명", example = "홍길동")
    @Size(min = 2, max = 5)
    @NotNull
    @Pattern(regexp = "[가-힣]*")
    private String name;

    @Schema(description = "휴대폰번호", example = "01013245673")
    @NotNull
    @Pattern(regexp = "^010\\d{4}\\d{4}$")
    private String phone;

    @Schema(description = "이메일", example = "abc@naver.com")
    @Email
    @NotNull
    private String email;

    @Schema(description = "유저 프로필 이미지 식별 ID", example = "2")
    private Long profileImageId;
}
