package com.fitmate.domain.redis.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;


@RedisHash(value = "notice")
@Getter
public class Notice {

    @Value("${redis.notice.expiration}")
    public Long DEFAULT_TTL;

    @Id
    private Long id;

    @Indexed
    private Long accountId;

    private Long matingId;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @TimeToLive
    private Long expiration = DEFAULT_TTL;

    @Builder
    public Notice(Long accountId, String content, Long matingId) {
        this.accountId = accountId;
        this.content = content;
        this.matingId = matingId;
    }
}
