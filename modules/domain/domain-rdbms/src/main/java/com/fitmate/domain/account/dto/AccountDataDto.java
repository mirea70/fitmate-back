package com.fitmate.domain.account.dto;

import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDataDto {
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
}
