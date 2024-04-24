package com.fitmate.domain.error.results;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotFoundErrorResult {
    NOT_FOUND_ACCOUNT_DATA("존재하지 않는 회원입니다."),
    NOT_FOUND_FILE_DATA("존재하지 않는 파일입니다."),
    NOT_EXIST_FILE_PATH("존재하지 않는 파일 경로입니다."),
    NOT_FOUND_ENUM_DATA("존재하지 않는 열거형 값입니다."),
    NOT_FOUND_MATE_DATA("존재하지 않는 메이트 글입니다."),
    NOT_FOUND_MATE_REQUEST_DATA("존재하지 않는 메이트 신청입니다."),
    NOT_FOUND_VALIDATE_DATA("존재하지 않는 인증 코드입니다."),
    NOT_FOUND_CHAT_ROOM_DATA("존재하지 않는 채팅방입니다."),
    ;

    private final String message;
}
