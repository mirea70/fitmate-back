package com.fitmate.account.dto;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import lombok.Builder;
import lombok.Getter;

public class AccountDto {
    @Getter
    @Builder
    public static class JoinRequest {

        private String name;

        private String loginName;

        private String password;

        private String nickName;

        private String introduction;

        private String phone;

        private String email;

        private AccountRole role;

        private Gender gender;
    }
}
