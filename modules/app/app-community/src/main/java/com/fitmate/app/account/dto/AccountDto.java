package com.fitmate.app.account.dto;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.entity.vo.PrivateInfo;
import com.fitmate.domain.account.entity.vo.ProfileInfo;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRequest {

        @NotNull(message = "로그인ID 입력은 필수입니다.")
        private String loginName;
        @NotNull(message = "비밀번호 입력은 필수입니다.")
        @Size(min = 8)
        private String password;
        @Valid
        @NotNull(message = "프로필 정보 입력은 필수입니다.")
        private ProfileInfo profileInfo;
        @Valid
        @NotNull(message = "개인정보 입력은 필수입니다.")
        private PrivateInfo privateInfo;
        @NotNull(message = "권한 입은 필수입니다.")
        private AccountRole role;
        @NotNull(message = "성별 입력은 필수입니다.")
        private Gender gender;
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
}
