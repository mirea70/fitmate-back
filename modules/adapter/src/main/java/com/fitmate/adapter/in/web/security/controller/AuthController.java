package com.fitmate.adapter.in.web.security.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.security.dto.KakaoLoginResponse;
import com.fitmate.adapter.in.web.security.dto.KakaoRegisterRequest;
import com.fitmate.adapter.in.web.security.dto.KakaoTokenResponse;
import com.fitmate.adapter.in.web.security.service.AuthService;
import com.fitmate.adapter.in.web.security.service.KakaoAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@WebAdapter
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "00. Auth", description = "인증 관리 API")
public class AuthController {

    private final AuthService authService;
    private final KakaoAuthService kakaoAuthService;

    @Operation(
            summary = "토큰 갱신",
            description = "액세스 토큰 갱신 API",
            parameters = {
                    @Parameter(name = "refresh",
                            description = "액세스 토큰 재발급을 위한 리프레시 토큰",
                            required = true,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string"))
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("refresh") String refreshToken,
                                     HttpServletResponse response) {
        authService.refresh(refreshToken, response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "카카오 로그인", description = "카카오 액세스 토큰으로 기존 유저 로그인 또는 신규 유저 확인")
    @PostMapping("/kakao")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestBody Map<String, String> body) {
        String kakaoAccessToken = body.get("accessToken");
        if (kakaoAccessToken == null || kakaoAccessToken.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(kakaoAuthService.kakaoLogin(kakaoAccessToken));
    }

    @Operation(summary = "카카오 회원가입", description = "카카오 토큰 + 프로필 정보로 계정 생성 후 JWT 토큰 발급")
    @PostMapping("/kakao/register")
    public ResponseEntity<KakaoTokenResponse> kakaoRegister(@Valid @RequestBody KakaoRegisterRequest request) {
        return ResponseEntity.ok(kakaoAuthService.kakaoRegister(request));
    }
}
