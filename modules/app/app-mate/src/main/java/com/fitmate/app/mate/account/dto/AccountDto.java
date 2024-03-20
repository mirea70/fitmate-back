package com.fitmate.app.mate.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import lombok.*;
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
    public static class JoinRequest {

        @NotNull(message = "로그인 ID 입력은 필수입니다.")
        private String loginName;
        @NotNull(message = "비밀번호 입력은 필수입니다.")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "비밀번호는 8자리 이상 영문자 소문자, 대문자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        private String password;
        @Valid
        @NotNull(message = "프로필 정보 입력은 필수입니다.")
        private ProfileInfo profileInfo;
        @Valid
        @NotNull(message = "개인정보 입력은 필수입니다.")
        private PrivateInfo privateInfo;
        @NotNull(message = "권한 입력은 필수입니다.")
        private AccountRole role;
        @NotNull(message = "성별 입력은 필수입니다.")
        private Gender gender;
        @JsonIgnore
        private MultipartFile profileImage;

    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponse {

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
    public static class UpdateRequest {

        @JsonIgnore
        private Long accountId;

        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",
                message = "비밀번호는 8자리 이상 영문자 소문자, 대문자, 특수문자를 각각 하나 이상 포함해야 합니다.")
        private String password;
        private ProfileInfo profileInfo;
        private PrivateInfo privateInfo;
        @JsonIgnore
        private MultipartFile profileImage;
    }
}
