package com.fitmate.app.mate.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AccountDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String loginName;

        private String password;

        private ProfileInfo profileInfo;

        private PrivateInfo privateInfo;

        private AccountRole role;

        private Gender gender;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 요청 DTO")
    public static class JoinRequest {

        @Schema(description = "로그인 ID (중복 불가)", example = "abc2")
        @NotNull(message = "로그인 ID 입력은 필수입니다.")
        private String loginName;

        @Schema(description = "로그인 패스워드 (필수, 8자리 이상 숫자/영소대문자/특수문자를 각각 하나 이상 포함해야함)", example = "!Qqweras33!!")
        @NotNull(message = "비밀번호 입력은 필수입니다.")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "비밀번호는 8자리 이상 영문자 소문자, 대문자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        private String password;

        @Schema(description = "회원 프로필 정보")
        @Valid
        @NotNull(message = "프로필 정보 입력은 필수입니다.")
        private ProfileInfo profileInfo;

        @Schema(description = "회원 개인 정보")
        @Valid
        @NotNull(message = "개인정보 입력은 필수입니다.")
        private PrivateInfo privateInfo;

        @Schema(description = "유저 권한 정보", example = "ADMIN")
        @NotNull(message = "권한 입력은 필수입니다.")
        private AccountRole role;

        @Schema(description = "유저 성별 정보", example = "MALE")
        @NotNull(message = "성별 입력은 필수입니다.")
        private Gender gender;

        @JsonIgnore
        private MultipartFile profileImage;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 반환 DTO")
    public static class JoinResponse {

        @Schema(description = "로그인 ID", example = "abc2")
        private String loginName;
        @Schema(description = "로그인 패스워드", example = "!Qqweras33!!")
        private String password;
        @Schema(description = "회원 프로필 정보")
        private ProfileInfo profileInfo;
        @Schema(description = "회원 개인 정보")
        private PrivateInfo privateInfo;
        @Schema(description = "유저 권한 정보", example = "ADMIN")
        private AccountRole role;
        @Schema(description = "유저 성별 정보", example = "MALE")
        private Gender gender;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 프로필 변경 요청 DTO")
    public static class UpdateRequest {

        @JsonIgnore
        private Long accountId;

        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "비밀번호는 8자리 이상 영문자 소문자, 대문자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        @Valid
        @Schema(description = "회원 프로필 정보")
        private ProfileInfo profileInfo;
        @Valid
        @Schema(description = "회원 개인 정보")
        private PrivateInfo privateInfo;
        @JsonIgnore
        private MultipartFile profileImage;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 팔로우 변경 요청 DTO")
    public static class FollowResponse {

        @Schema(description = "회원 식별 ID", example = "1")
        private Long accountId;
        @Schema(description = "회원 프로필 이미지 식별 ID", example = "1")
        private Long profileImageId;
        @Schema(description = "회원 닉네임", example = "자두")
        private String nickName;
    }
}
