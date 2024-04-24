package com.fitmate.port.in.account.command;

import com.fitmate.domain.account.vo.AccountRole;
import com.fitmate.domain.account.vo.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountJoinCommand implements AccountCheckCommand {

    private String loginName;
    private String password;
    private String nickName;
    private String introduction;
    private String name;
    private String phone;
    private String email;
    private AccountRole role;
    private Gender gender;
    private Long profileImageId;
}
