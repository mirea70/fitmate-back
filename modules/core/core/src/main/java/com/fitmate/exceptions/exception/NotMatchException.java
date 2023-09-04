package com.fitmate.exceptions.exception;

import com.fitmate.exceptions.result.NotMatchErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotMatchException extends RuntimeException {
    private final NotMatchErrorResult errorResult;
}
