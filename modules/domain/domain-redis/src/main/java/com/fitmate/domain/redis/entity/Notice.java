package com.fitmate.domain.redis.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;


@RedisHash(value = "notice")
@Getter
public class Notice {

    @Transient
    private final Long DEFAULT_TTL = 86400 * 30L;

    @Id
    private Long id;

    @Indexed
    private final Long accountId;

    private final Long matingId;

    private final String content;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @TimeToLive
    private final Long expiration;

    @Builder
    public Notice(Long accountId, String content, Long matingId) {
        this.accountId = accountId;
        this.content = content;
        this.matingId = matingId;
        this.expiration = DEFAULT_TTL;
    }
}
