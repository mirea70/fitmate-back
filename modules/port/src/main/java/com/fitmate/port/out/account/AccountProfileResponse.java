package com.fitmate.port.out.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class AccountProfileResponse {
    private final Long accountId;
    private final String loginName;
    private final String nickName;
    private final String introduction;
    private final Long profileImageId;
    private final String name;
    private final String phone;
    private final String email;
    private final String role;
    private final String gender;
    private Set<Long> followings;
    private Set<Long> followers;
}
