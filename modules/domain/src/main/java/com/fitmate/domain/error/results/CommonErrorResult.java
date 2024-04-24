package com.fitmate.domain.error.results;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorResult {
    UNKNOWN_EXCEPTION("Unknown Exception");

    private final String message;
}
