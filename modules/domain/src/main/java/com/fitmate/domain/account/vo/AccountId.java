package com.fitmate.domain.account.vo;

import lombok.Getter;

@Getter
public class AccountId {
    private final Long value;

    public AccountId(Long value) {
        this.value = value;
    }
}
