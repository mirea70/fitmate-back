package com.fitmate.domain.redis.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash(value = "notice")
@Getter
public class Notice {

    @Value("${redis.notice.expiration}")
    public Long DEFAULT_TTL;

    @Id
    private Long accountId;

    private Long matingId;

    private String content;

    @TimeToLive
    private Long expiration = DEFAULT_TTL;

    @Builder
    public Notice(Long accountId, Long matingId, String content) {
        this.accountId = accountId;
        this.matingId = matingId;
        this.content = content;
    }
}
