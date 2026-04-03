package com.fitmate.adapter.in.web.account.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.account.service.AccountRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@WebAdapter
@RestController
@RequestMapping("/api/account/recovery")
@RequiredArgsConstructor
@Tag(name = "01-03. Account Recovery", description = "계정 찾기/비밀번호 재설정 API")
public class AccountRecoveryController {

    private final AccountRecoveryService accountRecoveryService;

    @Operation(summary = "아이디 찾기", description = "전화번호로 로그인 ID 조회 (마스킹 처리)")
    @PostMapping("/find-id")
    public ResponseEntity<Map<String, String>> findLoginName(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(accountRecoveryService.findLoginName(body.get("phone")));
    }

    @Operation(summary = "비밀번호 재설정 - 인증번호 요청", description = "전화번호로 SMS 인증번호 발송")
    @PostMapping("/request-code")
    public ResponseEntity<?> requestCode(@RequestBody Map<String, String> body) {
        accountRecoveryService.requestRecoveryCode(body.get("phone"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 재설정 - 인증번호 확인", description = "SMS 인증번호 검증")
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {
        accountRecoveryService.verifyRecoveryCode(body.get("phone"), body.get("code"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 재설정 - 새 비밀번호 설정", description = "인증 완료 후 새 비밀번호 설정")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        accountRecoveryService.resetPassword(body.get("phone"), body.get("newPassword"));
        return ResponseEntity.ok().build();
    }
}
