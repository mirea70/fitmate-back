package com.fitmate.app.account.controller;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.app.account.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ResponseEntity<AccountDto.JoinResponse> join(@Valid @RequestBody AccountDto.JoinRequest joinRequest) {
        AccountDto.JoinResponse joinResponse = joinService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(joinResponse);
    }
}
