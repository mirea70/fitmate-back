package com.fitmate.adapter.in.web.security.controller;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.in.web.security.dto.KakaoRegisterRequest;
import com.fitmate.adapter.in.web.security.dto.KakaoTokenResponse;
import com.fitmate.adapter.in.web.security.service.AuthService;
import com.fitmate.adapter.in.web.security.service.KakaoAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("AuthController 테스트")
class AuthControllerTest extends BaseControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private KakaoAuthService kakaoAuthService;

    @Nested
    @DisplayName("POST /api/auth/kakao/register")
    class KakaoRegister {

        @Test
        @DisplayName("카카오 회원가입 — 201 Created")
        void kakaoRegisterSuccess() throws Exception {
            KakaoRegisterRequest request = new KakaoRegisterRequest(
                    "kakao-access-token",
                    "홍길동",
                    "MALE",
                    "1995-03-15",
                    "01012345678",
                    "홍시",
                    "abc@naver.com"
            );
            given(kakaoAuthService.kakaoRegister(any()))
                    .willReturn(new KakaoTokenResponse("access-token", "refresh-token"));

            mockMvc.perform(post("/api/auth/kakao/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.accessToken").value("access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
        }
    }
}
