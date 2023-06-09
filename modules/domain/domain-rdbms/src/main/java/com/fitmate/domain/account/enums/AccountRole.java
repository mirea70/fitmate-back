package com.fitmate.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountRole {
    ADMIN("관리자"),
    USER("회원"),
    ;

    private final String label;
}
