package com.fitmate.app.account.controller;

import com.fitmate.app.account.dto.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/api/account")
public class AccountController {
    @PostMapping("/join")
    public ResponseEntity<AccountDto.JoinResponse> join(@Valid @RequestBody AccountDto.JoinRequest joinRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
