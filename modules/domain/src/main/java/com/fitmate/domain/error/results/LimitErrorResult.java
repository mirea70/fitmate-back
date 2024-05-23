package com.fitmate.domain.error.results;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LimitErrorResult {
    OVER_MATE_PEOPLE_LIMIT("가능한 메이트 인원 제한수를 초과하였습니다."),
    OVER_PERMIT_FILE_COUNT("가능한 업로드 파일 제한수를 초과하였습니다."),
    ;

    private final String message;
}
