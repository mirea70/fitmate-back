package com.fitmate.domain.error.exceptions;

import com.fitmate.domain.error.results.FileErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileRequestException extends RuntimeException {
    private final FileErrorResult errorResult;
}
