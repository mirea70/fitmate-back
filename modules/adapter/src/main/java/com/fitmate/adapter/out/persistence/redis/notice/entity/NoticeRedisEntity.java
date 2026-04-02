package com.fitmate.adapter.out.persistence.redis.notice.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@RedisHash(value = "notice")
@Getter
public class NoticeRedisEntity {

    @Id
    private Long id;

    @Indexed
    private final Long accountId;

    private final Long matingId;

    private final Long senderAccountId;

    private final String content;

    private final String noticeType;

    @Indexed
    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @TimeToLive
    private final Long expiration;

    public NoticeRedisEntity(Long accountId, Long matingId, Long senderAccountId, String content, String noticeType, Long expiration) {
        this.accountId = accountId;
        this.matingId = matingId;
        this.senderAccountId = senderAccountId;
        this.content = content;
        this.noticeType = noticeType;
        this.expiration = expiration;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
