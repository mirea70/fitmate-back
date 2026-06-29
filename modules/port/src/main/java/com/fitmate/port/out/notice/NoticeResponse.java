package com.fitmate.port.out.notice;

import java.time.LocalDateTime;

public record NoticeResponse(
        Long id,
        Long matingId,
        Long senderAccountId,
        String content,
        String noticeType,
        LocalDateTime createdAt
) {
}
