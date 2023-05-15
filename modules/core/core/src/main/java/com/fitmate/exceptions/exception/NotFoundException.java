package com.fitmate.exceptions.exception;

import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException{
    private final NotFoundErrorResult errorResult;
}
