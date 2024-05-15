package com.fitmate.adapter.in.web.sms.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.sms.dto.SmsCodeRequest;
import com.fitmate.port.in.sms.usecase.SmsUseCasePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@WebAdapter
@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Tag(name = "04. Sms", description = "문자 및 이메일 관리 API")
public class SmsController {
    private final SmsUseCasePort smsUseCasePort;

    @Operation(summary = "문자 인증 요청", description = """
            문자 인증 요청 API (유효기간 60초) : 토큰 필요 X
            
            **-> 현재 실제 문자 요청은 요금 부담으로 인해 차단해놓았습니다.**
            """)
    @PostMapping("/request/code")
    public ResponseEntity<?> requestValidateCode(@Valid @RequestBody SmsCodeRequest request) {
        smsUseCasePort.requestValidateCode(request.getPhone());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증번호 체크", description = "인증번호 체크 API : 토큰 필요 X")
    @GetMapping("/check/code")
    public ResponseEntity<?> checkValidateCode(@Parameter(description = "사용자 입력값 (인증번호)")
                                               @RequestParam String inputCode) {
        smsUseCasePort.checkValidateCode(inputCode);
        return ResponseEntity.ok().build();
    }
}
