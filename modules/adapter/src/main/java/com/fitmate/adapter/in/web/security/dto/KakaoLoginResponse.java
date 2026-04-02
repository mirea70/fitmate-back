package com.fitmate.adapter.in.web.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoLoginResponse {
    private final boolean newUser;
    private final String accessToken;
    private final String refreshToken;
    private final String kakaoNickName;
    private final String kakaoEmail;

    public static KakaoLoginResponse existingUser(String accessToken, String refreshToken) {
        return new KakaoLoginResponse(false, accessToken, refreshToken, null, null);
    }

    public static KakaoLoginResponse newUser(String kakaoNickName, String kakaoEmail) {
        return new KakaoLoginResponse(true, null, null, kakaoNickName, kakaoEmail);
    }
}
