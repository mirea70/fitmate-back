package com.fitmate.domain.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "validate_code")
@Getter
public class ValidateCode {

    @Transient
    private final Long DEFAULT_TTL = 60L;

    @Id
    private String code;

    @TimeToLive
    public Long expiration;

    @Builder
    public ValidateCode(String code) {
        this.code = code;
        this.expiration = DEFAULT_TTL;
    }
}
