package com.fitmate.account.dto;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import lombok.Builder;
import lombok.Getter;

public class AccountDto {
    @Getter
    @Builder
    public static class JoinRequest {

        private String loginName;

        private String password;

        private ProfileInfo profileInfo;

        private PrivateInfo privateInfo;

        private AccountRole role;

        private Gender gender;
    }
    @Getter
    @Builder
    public static class JoinResponse {

        private String loginName;

        private String password;

        private ProfileInfo profileInfo;

        private PrivateInfo privateInfo;

        private AccountRole role;

        private Gender gender;
    }
}
