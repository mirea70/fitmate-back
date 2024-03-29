package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.account.service.AccountValidateService;
import com.fitmate.system.security.dto.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "01-03. Account Validate", description = "회원 인증 관련 API")
@RestController
@RequestMapping("/api/accounts/validate")
@RequiredArgsConstructor
public class AccountValidateController {

    private final AccountValidateService accountValidateService;

    @Operation(summary = "현재 비밀번호 체크", description = "현재 비밀번호 체크 API")
    @GetMapping("/current/password")
    public ResponseEntity<?> validateCurrentPassword(@AuthenticationPrincipal AuthDetails authDetails,
                                                     @Parameter(description = "사용자 입력값 (현재 비밀번호)")
                                                     @RequestParam String inputPassword) {
        accountValidateService.validateCurrentPassword(authDetails.getAccount().getId(), inputPassword);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문자 인증 요청", description = "문자 인증 요청 API (유효기간 60초)")
    @PostMapping("/request/code")
    public ResponseEntity<?> requestValidateCode(@AuthenticationPrincipal AuthDetails authDetails) {
        accountValidateService.requestValidateCode(authDetails.getAccount().getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증번호 체크", description = "인증번호 체크 API")
    @GetMapping("/check/code")
    public ResponseEntity<?> checkValidateCode(@Parameter(description = "사용자 입력값 (인증번호)")
                                                   @RequestParam String inputCode) {
        accountValidateService.checkValidateCode(inputCode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "새 비밀번호 저장", description = "새 비밀번호 저장 API")
    @PostMapping("/new/password")
    public ResponseEntity<?> saveNewPassword(@AuthenticationPrincipal AuthDetails authDetails,
                                             @Parameter(description = "사용자 입력값 (새 비밀번호)")
                                             @RequestParam String newPassword) {
        accountValidateService.saveNewPassword(authDetails.getAccount().getId(), newPassword);
        return ResponseEntity.ok().build();
    }
}
