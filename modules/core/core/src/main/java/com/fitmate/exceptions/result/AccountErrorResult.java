package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountErrorResult {
    DUPLICATED_ACCOUNT_JOIN("Duplicated Account Join Request"),
    DUPLICATED_ACCOUNT_VALUE("Duplicated Account Value Request")
    ;

    private final String message;
}
