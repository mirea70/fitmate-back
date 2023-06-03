package com.fitmate.domain.redis.helper;

import com.fitmate.domain.redis.entity.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenTestHelper {
    public RefreshToken getTestRefreshToken() {
        return RefreshToken.builder()
                .token("token1")
                .email("email@naver.com")
                .build();
    }
}
