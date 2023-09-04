package com.fitmate.exceptions.exception;

import com.fitmate.exceptions.result.DuplicatedErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicatedException extends RuntimeException{
    private final DuplicatedErrorResult errorResult;
}
