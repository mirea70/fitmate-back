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

    private final String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @TimeToLive
    private final Long expiration;

    public NoticeRedisEntity(Long accountId, String content, Long matingId, Long expiration) {
        this.accountId = accountId;
        this.content = content;
        this.matingId = matingId;
        this.expiration = expiration;
    }
}
