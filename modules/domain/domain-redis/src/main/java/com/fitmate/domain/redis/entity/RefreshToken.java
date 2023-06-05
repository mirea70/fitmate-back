package com.fitmate.domain.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("refresh")
@Getter
public class RefreshToken {
    @Id
    private String email;

    private String token;

    @Builder
    public RefreshToken(String email, String token) {
        this.token = token;
        this.email = email;
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
