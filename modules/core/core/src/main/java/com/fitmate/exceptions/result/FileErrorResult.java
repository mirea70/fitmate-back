package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileErrorResult {
    NOT_MATCHING_NAME_RULE("파일 이름은 특수문자를 제외해야합니다."),
    NOT_SUPPORT_EXT("지원하지 않는 확장자입니다.")
    ;

    private final String message;
}
