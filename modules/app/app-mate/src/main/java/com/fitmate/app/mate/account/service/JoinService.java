package com.fitmate.app.mate.account.service;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.result.DuplicatedErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileService fileService;

    public AccountDto.JoinResponse join(AccountDto.JoinRequest joinRequest) throws IOException {

        Account newAccount = setJoinData(joinRequest);

        try {
            Account savedAccount = accountRepository.save(newAccount);
            return AccountDtoMapper.INSTANCE.toResponse(savedAccount);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_ACCOUNT_VALUE);
        }
    }

    private Account setJoinData(AccountDto.JoinRequest joinRequest) throws IOException {
        checkDuplicated(joinRequest);
        joinRequest.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
        uploadProfileImage(joinRequest);

        return AccountDtoMapper.INSTANCE.toEntity(joinRequest);
    }

    private void checkDuplicated(AccountDto.JoinRequest joinRequest) {
        AccountDuplicateCheckDto checkDto = AccountDtoMapper.INSTANCE.toDuplicatedCheckDto(joinRequest);
        accountService.CheckDuplicated(checkDto);
    }

    private void uploadProfileImage(AccountDto.JoinRequest joinRequest) throws IOException {
        if(joinRequest.getProfileImage() != null) {
            AttachFileDto.Response fileResponse = fileService.uploadFile(joinRequest.getProfileImage());
            joinRequest.getProfileInfo().updateProfileImageId(fileResponse.getId());
        }
    }

}
