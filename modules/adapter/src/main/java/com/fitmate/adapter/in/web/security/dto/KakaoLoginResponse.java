package com.fitmate.adapter.in.web.security.dto;

public record KakaoLoginResponse(
        boolean newUser,
        String accessToken,
        String refreshToken,
        String kakaoNickName,
        String kakaoEmail
) {
    public static KakaoLoginResponse existingUser(String accessToken, String refreshToken) {
        return new KakaoLoginResponse(false, accessToken, refreshToken, null, null);
    }

    public static KakaoLoginResponse newUser(String kakaoNickName, String kakaoEmail) {
        return new KakaoLoginResponse(true, null, null, kakaoNickName, kakaoEmail);
    }
}
