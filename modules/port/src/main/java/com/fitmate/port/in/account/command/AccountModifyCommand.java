package com.fitmate.port.in.account.command;

public record AccountModifyCommand(
        Long accountId,
        String nickName,
        String introduction,
        String name,
        String phone,
        String email,
        Long profileImageId
) implements AccountCheckCommand {
}
