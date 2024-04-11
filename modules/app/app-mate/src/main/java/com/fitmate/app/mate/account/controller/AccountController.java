package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.account.service.JoinService;
import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "01-01. Account", description = "회원 관리 API")
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final JoinService joinService;

    private final AccountService accountService;

    @Operation(summary = "회원가입", description = "회원가입 API : Request body의 설명은 application/json 타입으로 봐주시고 테스트는 multipart/form-data로 실행해주세요.")
    @PostMapping(value = "/join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountDto.JoinResponse> join(@Valid @RequestPart AccountDto.JoinRequest joinRequest,
                                                        @RequestPart(required = false)
                                                        @Parameter(description = "회원 프로필 이미지 파일(1개)")
                                                        MultipartFile profileImage) throws Exception {
        joinRequest.setProfileImage(profileImage);
        AccountDto.JoinResponse joinResponse = joinService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(joinResponse);
    }

    @Operation(summary = "회원 단일 조회", description = "회원 단일 조회 API")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto.Response> find(@Parameter(description = "요청 회원 ID")
                                                        @PathVariable final Long accountId) {
        AccountDataDto.Response dataResponse = accountService.validateFindById(accountId);
        AccountDto.Response response = AccountDtoMapper.INSTANCE.toRealResponse(dataResponse);
        return ResponseEntity.ok(response);
    }
}
