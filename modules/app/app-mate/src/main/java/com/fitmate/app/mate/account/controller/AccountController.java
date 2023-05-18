package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.account.service.JoinService;
import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final JoinService joinService;

    private final AccountService accountService;

    @PostMapping("/join")
    public ResponseEntity<AccountDto.JoinResponse> join(@Valid @RequestBody AccountDto.JoinRequest joinRequest) throws Exception {
        AccountDto.JoinResponse joinResponse = joinService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(joinResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto.Response> find(@PathVariable final Long id) throws Exception {
        AccountDataDto.Response dataResponse = accountService.validateFindById(id);
        AccountDto.Response response = AccountDtoMapper.INSTANCE.toRealResponse(dataResponse);
        return ResponseEntity.ok(response);
    }
}
