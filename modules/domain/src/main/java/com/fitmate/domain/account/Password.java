package com.fitmate.domain.account;

import lombok.Getter;

@Getter
public class Password {
    private final String value;

    public Password(String value) {
        this.value = value;
    }
}
