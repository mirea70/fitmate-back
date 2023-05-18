package com.fitmate.domain.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MAIL("남성"),
    FEMALE("여성"),
    ;

    private final String label;
}
