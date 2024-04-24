package com.fitmate.domain.error.exceptions;

import com.fitmate.domain.error.results.DuplicatedErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicatedException extends RuntimeException{
    private final DuplicatedErrorResult errorResult;
}
