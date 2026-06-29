package com.fitmate.port.in.account.command;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;

import java.time.LocalDate;

public record AccountJoinCommand(
        String loginName,
        String password,
        String nickName,
        String introduction,
        String name,
        String phone,
        String email,
        LocalDate birthDate,
        AccountRole role,
        Gender gender
) implements AccountCheckCommand {
}
