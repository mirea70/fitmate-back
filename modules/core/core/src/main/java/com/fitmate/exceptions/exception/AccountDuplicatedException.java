package com.fitmate.exceptions.exception;

import com.fitmate.exceptions.result.AccountErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDuplicatedException extends RuntimeException{
    private final AccountErrorResult errorResult;
}
