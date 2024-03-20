package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotFoundErrorResult {
    NOT_FOUND_ACCOUNT_DATA("존재하지 않는 회원입니다."),
    NOT_FOUND_FILE_DATA("존재하지 않는 파일입니다."),
    NOT_EXIST_FILE_PATH("존재하지 않는 파일 경로입니다."),
    NOT_FOUND_ENUM_DATA("존재하지 않는 열거형 값입니다."),
    NOT_FOUND_MATING_DATA("존재하지 않는 메이팅 글입니다."),
    ;

    private final String message;
}
