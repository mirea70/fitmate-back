package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotFoundErrorResult {
    NOT_FOUNT_ACCOUNT_DATA("존재하지 않는 회원입니다."),
    ;

    private final String message;
}
