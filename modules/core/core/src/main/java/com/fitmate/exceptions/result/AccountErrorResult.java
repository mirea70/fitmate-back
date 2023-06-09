package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountErrorResult {
    DUPLICATED_ACCOUNT_JOIN("중복된 회원에 대한 요청입니다."),
    DUPLICATED_ACCOUNT_VALUE("요청 값 중, 기존 회원과 중복된 값이 존재합니다."),
    NOT_FOUNT_ACCOUNT_DATA("존재하지 않는 회원입니다."),
    ;

    private final String message;
}
