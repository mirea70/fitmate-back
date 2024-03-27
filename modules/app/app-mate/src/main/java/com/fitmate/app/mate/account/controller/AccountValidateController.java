package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.account.service.AccountValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/validate")
@RequiredArgsConstructor
public class AccountValidateController {

    private final AccountValidateService accountValidateService;

    @GetMapping("/current/password")
    public ResponseEntity<?> validateCurrentPassword(@PathVariable Long accountId,
                                                     @RequestParam String inputPassword) {
        accountValidateService.validateCurrentPassword(accountId, inputPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request/code")
    public ResponseEntity<?> requestValidateCode(@PathVariable Long accountId) {
        accountValidateService.requestValidateCode(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/code")
    public ResponseEntity<?> checkValidateCode(@PathVariable Long accountId,
                                                @RequestParam String inputCode) {
        accountValidateService.checkValidateCode(inputCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new/password")
    public ResponseEntity<?> saveNewPassword(@PathVariable Long accountId,
                                             @RequestParam String newPassword) {
        accountValidateService.saveNewPassword(accountId, newPassword);
        return ResponseEntity.ok().build();
    }
}
