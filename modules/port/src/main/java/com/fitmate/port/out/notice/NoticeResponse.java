package com.fitmate.port.out.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NoticeResponse {
    private final Long id;
    private final Long matingId;
    private final Long senderAccountId;
    private final String content;
    private final String noticeType;
    private final LocalDateTime createdAt;
}
