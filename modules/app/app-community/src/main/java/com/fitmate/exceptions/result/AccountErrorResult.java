package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountErrorResult {
    DUPLICATED_ACCOUNT_JOIN(HttpStatus.BAD_REQUEST, "Duplicated Account Join Request"),
    DUPLICATED_ACCOUNT_VALUE(HttpStatus.BAD_REQUEST, "Duplicated Account Value Request")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
