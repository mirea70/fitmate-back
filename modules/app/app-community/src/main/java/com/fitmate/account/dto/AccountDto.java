package com.fitmate.account.dto;

import com.fitmate.enums.AccountRole;

public class AccountDto {
    public static class JoinRequest {

        private String name;

        private String loginName;

        private String password;

        private String nickName;

        private String phone;

        private String email;

        private AccountRole role;
    }
}
