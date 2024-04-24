package com.fitmate.domain.error.exceptions;

import com.fitmate.domain.error.results.LimitErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LimitException extends RuntimeException {
    private final LimitErrorResult errorResult;
}
