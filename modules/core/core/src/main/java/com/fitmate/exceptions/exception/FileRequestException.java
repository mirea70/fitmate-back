package com.fitmate.exceptions.exception;

import com.fitmate.exceptions.result.FileErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileRequestException extends RuntimeException {
    private final FileErrorResult errorResult;
}
