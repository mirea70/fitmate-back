package com.fitmate.domain.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "validate_code")
@Getter
public class ValidateCode {
    @Value("${redis.validate.expiration}")
    private Long DEFAULT_TTL;

    @Id
    private String code;

    @TimeToLive
    public Long expiration = DEFAULT_TTL;

    @Builder
    public ValidateCode(String code) {
        this.code = code;
    }
}
