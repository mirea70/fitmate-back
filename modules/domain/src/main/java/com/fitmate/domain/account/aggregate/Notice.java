package com.fitmate.domain.account.aggregate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice {

    private static final Long DEFAULT_TTL = 86400 * 30L;

    private final Long id;

    private final Long accountId;

    private final Long matingId;

    private final String content;

    private final LocalDateTime createdAt;

    private final Long expiration;

    public static Notice withOutMatingId(Long accountId, String content) {
        return new Notice(null, accountId, null, content, null, DEFAULT_TTL);
    }

    public static Notice withMatingId(Long accountId, Long matingId, String content) {
        return new Notice(null, accountId, matingId, content, null, DEFAULT_TTL);
    }
}
