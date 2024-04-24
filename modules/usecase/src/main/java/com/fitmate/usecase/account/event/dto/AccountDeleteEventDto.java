package com.fitmate.usecase.account.event.dto;

import lombok.Getter;

@Getter
public class AccountDeleteEventDto {
    private final Long accountId;

    public AccountDeleteEventDto(Long accountId) {
        this.accountId = accountId;
    }
}
