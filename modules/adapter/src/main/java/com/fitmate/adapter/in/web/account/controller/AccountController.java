package com.fitmate.adapter.in.web.account.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.account.dto.AccountJoinRequest;
import com.fitmate.adapter.in.web.account.mapper.AccountWebAdapterMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.out.account.AccountProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@WebAdapter
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "01-01. Account", description = "회원 관리 API")
public class AccountController {

    private final AccountProfileUseCasePort accountProfileUseCasePort;
    private final AccountWebAdapterMapper accountWebAdapterMapper;

    @Operation(summary = "회원가입", description = """
            회원가입 API : 토큰 필요 X
            
            **[참고]** : profileImageId를 입력하려면 파일 관리 API를 통한 파일 업로드를 선행해주세요.
            """)
    @PostMapping(path = "/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> join(@Valid @RequestBody AccountJoinRequest joinRequest) throws Exception {

        accountProfileUseCasePort.join(accountWebAdapterMapper.requestToCommand(joinRequest));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그인 ID 중복체크", description = "회원가입 시, 로그인 ID 중복 체크 API")
    @GetMapping(path = "/check/loginName")
    public ResponseEntity<?> checkDuplicatedLoginName(@Parameter(description = "로그인 ID : 2~20자의 영문+숫자")
                                                          @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{1,20}$")
                                                          @RequestParam String loginName) {
        accountProfileUseCasePort.checkDuplicatedLoginName(loginName);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "휴대폰번호 중복체크", description = "회원가입 시, 휴대폰번호 중복 체크 API")
    @GetMapping(path = "/check/phone")
    public ResponseEntity<?> checkDuplicatedPhone(@Parameter(description = "휴대폰번호 : 01011112222 (11자)")
                                                      @Pattern(regexp = "^010\\d{4}\\d{4}$")
                                                      @RequestParam String phone) {
        accountProfileUseCasePort.checkDuplicatedPhone(phone);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 정보 조회", description = "자신의 정보를 조회하는 API")
    @GetMapping
    ResponseEntity<AccountProfileResponse> findOne(@AuthenticationPrincipal AuthDetails authDetails) {
        AccountProfileResponse response = accountProfileUseCasePort.findAccount(authDetails.getAccount().getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 API")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> delete(@Parameter(description = "요청 회원 ID")
                                    @PathVariable final Long accountId) {
        accountProfileUseCasePort.delete(accountId);
        return ResponseEntity.ok().build();
    }
}
