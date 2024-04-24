package com.fitmate.port.out.notice;

import lombok.Getter;

@Getter
public class NoticeResponse {
    private final Long id;
    private final Long matingId;
    private final String content;

    public NoticeResponse(Long id, Long matingId, String content) {
        this.id = id;
        this.matingId = matingId;
        this.content = content;
    }
}
