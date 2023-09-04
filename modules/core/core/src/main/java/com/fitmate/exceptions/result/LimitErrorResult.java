package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LimitErrorResult {
    OVER_MATE_PEOPLE_LIMIT("가능한 메이트 인원 제한수를 초과하였습니다."),
    ;

    private final String message;
}
