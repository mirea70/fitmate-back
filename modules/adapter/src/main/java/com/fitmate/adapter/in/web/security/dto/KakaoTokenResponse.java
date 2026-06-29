package com.fitmate.adapter.in.web.security.dto;

public record KakaoTokenResponse(
        String accessToken,
        String refreshToken
) {
}
