package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DuplicatedErrorResult {

    DUPLICATED_ACCOUNT_JOIN("중복된 회원에 대한 요청입니다."),
    DUPLICATED_ACCOUNT_VALUE("요청 값 중, 기존 회원과 중복된 값이 존재합니다."),
    DUPLICATED_MATE_REQUEST("이미 요청했던 메이트 모집 글입니다."),
    ;

    private final String message;
}
