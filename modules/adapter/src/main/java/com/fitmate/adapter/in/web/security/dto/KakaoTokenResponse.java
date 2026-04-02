package com.fitmate.adapter.in.web.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoTokenResponse {
    private final String accessToken;
    private final String refreshToken;
}
