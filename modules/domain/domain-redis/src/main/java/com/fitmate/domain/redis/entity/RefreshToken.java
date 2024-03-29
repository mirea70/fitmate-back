package com.fitmate.domain.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refresh")
@Getter
public class RefreshToken {

    @Transient
    private final Long DEFAULT_TTL = 86400 * 14L;

    @Id
    private String email;

    private String token;

    @TimeToLive
    public Long expiration;

    @Builder
    public RefreshToken(String email, String token) {
        this.token = token;
        this.email = email;
        this.expiration = DEFAULT_TTL;
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
