package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotMatchErrorResult {
    NOT_MATCH_WAIT_ACCOUNT_LIST("승인 요청 회원 ID 중, 대기 리스트에 없는 ID 값이 존재합니다."),
    ;

    private final String message;
}
