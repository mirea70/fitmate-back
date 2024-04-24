package com.fitmate.adapter.out.persistence.redis.code.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "validate_code")
@Getter
public class ValidateCodeRedisEntity {

    @Transient
    private final Long DEFAULT_TTL = 60L;

    @Id
    private final String code;

    @TimeToLive
    public Long expiration;

    public ValidateCodeRedisEntity(String code) {
        this.code = code;
        this.expiration = DEFAULT_TTL;
    }
}
