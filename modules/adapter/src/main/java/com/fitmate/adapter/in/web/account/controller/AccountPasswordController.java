package com.fitmate.adapter.in.web.account.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.port.in.account.usecase.AccountPasswordUseCasePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequestMapping("/api/account/password")
@RequiredArgsConstructor
@Tag(name = "01-03. Account Password", description = "회원 비밀번호 찾기 관련 API")
public class AccountPasswordController {

    private final AccountPasswordUseCasePort accountPasswordUseCasePort;

    @Operation(summary = "현재 비밀번호 체크", description = "현재 비밀번호 체크 API")
    @GetMapping("/current/password")
    public ResponseEntity<?> validateCurrentPassword(@AuthenticationPrincipal AuthDetails authDetails,
                                                     @Parameter(description = "사용자 입력값 (현재 비밀번호)")
                                                     @RequestParam String inputPassword) {
        accountPasswordUseCasePort.validateCurrentPassword(authDetails.getAccount().getId(), inputPassword);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문자 인증 요청", description = "문자 인증 요청 API (유효기간 60초) \n\n **-> 현재 실제 문자 요청은 요금 부담으로 인해 차단해놓았습니다.**")
    @PostMapping("/request/code")
    public ResponseEntity<?> requestValidateCode(@AuthenticationPrincipal AuthDetails authDetails) {
        accountPasswordUseCasePort.requestValidateCode(authDetails.getAccount().getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증번호 체크", description = "인증번호 체크 API")
    @GetMapping("/check/code")
    public ResponseEntity<?> checkValidateCode(@Parameter(description = "사용자 입력값 (인증번호)")
                                               @RequestParam String inputCode) {
        accountPasswordUseCasePort.checkValidateCode(inputCode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "새 비밀번호 저장", description = "새 비밀번호 저장 API")
    @PostMapping("/new/password")
    public ResponseEntity<?> saveNewPassword(@AuthenticationPrincipal AuthDetails authDetails,
                                             @Parameter(description = "사용자 입력값 (새 비밀번호)")
                                             @RequestParam String newPassword) {
        accountPasswordUseCasePort.saveNewPassword(authDetails.getAccount().getId(), newPassword);
        return ResponseEntity.ok().build();
    }
}
