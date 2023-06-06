package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotFoundErrorResult {
    NOT_FOUND_ACCOUNT_DATA("존재하지 않는 회원입니다."),
    NOT_FOUND_FILE_DATA("존재하지 않는 파일입니다."),
    ;

    private final String message;
}
