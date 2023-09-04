package com.fitmate.exceptions.exception;

import com.fitmate.exceptions.result.LimitErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LimitException extends RuntimeException {
    private final LimitErrorResult errorResult;
}
