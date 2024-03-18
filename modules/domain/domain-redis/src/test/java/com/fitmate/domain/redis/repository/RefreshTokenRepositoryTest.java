package com.fitmate.domain.redis.repository;

import com.fitmate.domain.redis.config.RedisConfig;
import com.fitmate.domain.redis.entity.RefreshToken;
import com.fitmate.domain.redis.helper.RefreshTokenTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataRedisTest
@Import({RedisConfig.class, RefreshTokenTestHelper.class})
@ActiveProfiles({"dev","redis"})
public class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private RefreshTokenTestHelper refreshTokenTestHelper;

    @Test
    public void 리프레시토큰_저장 () throws Exception {
        // given
        RefreshToken newRefreshToken = refreshTokenTestHelper.getTestRefreshToken();
        // when
        RefreshToken savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
        // then
        assertEquals(newRefreshToken.getEmail() ,savedRefreshToken.getEmail());
    }

    @Test
    public void 리프레시토큰_조회_Email () throws Exception {
        // given
        RefreshToken newRefreshToken = refreshTokenTestHelper.getTestRefreshToken();
        RefreshToken savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
        // when
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findById(savedRefreshToken.getEmail());
        // then
        assertTrue(findRefreshToken.isPresent());
        assertEquals(savedRefreshToken.getToken(), findRefreshToken.get().getToken());
    }
}
