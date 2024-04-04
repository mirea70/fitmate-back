package com.fitmate.exceptions.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DuplicatedErrorResult {

    DUPLICATED_ACCOUNT_JOIN("중복된 회원에 대한 요청입니다."),
    DUPLICATED_ACCOUNT_VALUE("요청 값 중, 기존 회원과 중복된 값이 존재합니다."),
    DUPLICATED_MATE_REQUEST("이미 요청했던 메이트 모집 글입니다."),
    DUPLICATED_MATE_APPLIER("이미 승인된 신청자가 존재합니다."),
    DUPLICATED_CHAT_ROOM_ABOUT_MATE("해당 메이팅 글에 대한 그룹 채팅방이 이미 존재합니다."),
    DUPLICATED_CHAT_ROOM_ABOUT_ACCOUNT("해당 회원이 포함된 DM 채팅방이 이미 존재합니다."),
    ;

    private final String message;
}
