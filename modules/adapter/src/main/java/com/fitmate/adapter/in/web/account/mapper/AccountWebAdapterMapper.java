package com.fitmate.adapter.in.web.account.mapper;

import com.fitmate.adapter.in.web.account.dto.AccountJoinRequest;
import com.fitmate.adapter.in.web.account.dto.AccountModifyRequest;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.command.AccountModifyCommand;
import org.springframework.stereotype.Component;

@Component
public class AccountWebAdapterMapper {
    public AccountJoinCommand requestToCommand(AccountJoinRequest request) {
        return new AccountJoinCommand(
                request.loginName(),
                request.password(),
                request.nickName(),
                request.introduction(),
                request.name(),
                request.phone(),
                request.email(),
                request.birthDate(),
                request.role(),
                request.gender()
        );
    }

    public AccountModifyCommand requestToCommand(Long accountId, AccountModifyRequest request) {
        return new AccountModifyCommand(
                accountId,
                request.nickName(),
                request.introduction(),
                request.name(),
                request.phone(),
                request.email(),
                request.profileImageId());
    }
}
