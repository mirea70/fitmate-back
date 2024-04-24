package com.fitmate.domain.error.exceptions;

import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException{
    private final NotFoundErrorResult errorResult;
}
