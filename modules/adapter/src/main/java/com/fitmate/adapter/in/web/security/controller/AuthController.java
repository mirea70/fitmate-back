package com.fitmate.adapter.in.web.security.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@WebAdapter
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "00. Auth", description = "인증 관리 API")
public class AuthController {
    private final AuthService authService;

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
}
