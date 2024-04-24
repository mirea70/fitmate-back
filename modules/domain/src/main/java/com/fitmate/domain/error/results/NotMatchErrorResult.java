package com.fitmate.domain.error.results;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotMatchErrorResult {
    NOT_MATCH_WAIT_ACCOUNT_LIST("승인 요청 회원 ID 중, 대기 리스트에 없는 ID 값이 존재합니다."),
    NOT_MATCH_CURRENT_PASSWORD("현재 비밀번호와 다릅니다."),
    NOT_MATCH_WRITER_ID("작성자 본인이 아닙니다."),
    CANNOT_APPLY_WRITER("작성자 본인은 메이트 신청을 할 수 없습니다."),
    ;

    private final String message;
}
