package com.fitmate.domain.error.exceptions;

import com.fitmate.domain.error.results.NotMatchErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotMatchException extends RuntimeException {
    private final NotMatchErrorResult errorResult;
}
