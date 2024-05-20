package com.fitmate.adapter.in.web.security.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import com.fitmate.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final TokenProvider tokenProvider;

    public void refresh(String rawToken, HttpServletResponse response) {
        String refreshToken = tokenProvider.resolveToken(rawToken);
        // refresh Token이 요청헤더에 있다면
        if (refreshToken != null) {
            // refresh Token을 검증
            TokenProvider.JwtCode refValidResult = tokenProvider.validateToken(refreshToken, "refresh");
            // 검증이 통과하면
            if (refValidResult != TokenProvider.JwtCode.DENIED) {
                // refresh Token 재발급
                String newRefreshToken = tokenProvider.updateRefreshToken(refreshToken);
                response.addHeader("refresh", "Bearer " + newRefreshToken);
                // Access Token 재발급
                Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
                response.addHeader("access", "Bearer " + tokenProvider.createAccessToken(authDetails.getAccount().getId(), authDetails.getEmail()));
                log.info("리프레시 토큰 & 액세스 토큰 최신화 완료");
            } else {
                throw new TokenExpiredException("토큰 만료", Instant.now());
            }
        } else
            throw new IllegalArgumentException("refresh token need");
    }
}
