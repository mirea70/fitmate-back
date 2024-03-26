package com.fitmate.app.mate.account.controller;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.account.service.AccountProfileService;
import com.fitmate.app.mate.account.service.JoinService;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final JoinService joinService;

    private final AccountService accountService;
    private final AccountProfileService accountProfileService;

    @PostMapping(value = "/join", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AccountDto.JoinResponse> join(@Valid @RequestPart AccountDto.JoinRequest joinRequest,
                                                        @RequestPart(required = false) MultipartFile profileImage) throws Exception {
        joinRequest.setProfileImage(profileImage);
        AccountDto.JoinResponse joinResponse = joinService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(joinResponse);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto.Response> find(@PathVariable final Long accountId) {
        AccountDataDto.Response dataResponse = accountService.validateFindById(accountId);
        AccountDto.Response response = AccountDtoMapper.INSTANCE.toRealResponse(dataResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/image")
    public ResponseEntity<UrlResource> downloadProfileImage(@PathVariable final Long accountId) throws MalformedURLException {
        AttachFileDto.Download downloadDto = accountProfileService.downloadProfileImage(accountId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, downloadDto.getContentDisposition())
                .body(downloadDto.getUrlResource());
    }

    @PatchMapping(value = "/{accountId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AccountDto.Response> updateProfile(@Valid @RequestPart AccountDto.UpdateRequest updateRequest,
                                                            @RequestPart(required = false) MultipartFile profileImage,
                                                             @PathVariable final Long accountId) throws Exception {
        updateRequest.setAccountId(accountId);
        updateRequest.setProfileImage(profileImage);
        return ResponseEntity.ok(accountProfileService.modifyProfile(updateRequest));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> delete(@PathVariable final Long accountId) {
        accountProfileService.remove(accountId);
        return ResponseEntity.ok().build();
    }
}
