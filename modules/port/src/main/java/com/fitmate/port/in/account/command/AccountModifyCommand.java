package com.fitmate.port.in.account.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountModifyCommand implements AccountCheckCommand {
    private Long accountId;
    private String nickName;
    private String introduction;
    private String name;
    private String phone;
    private String email;
    private Long profileImageId;
}
