package com.fitmate.app.mate.account.service;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileService fileService;

    public AccountDto.JoinResponse join(AccountDto.JoinRequest joinRequest) throws Exception {
        AccountDuplicateCheckDto checkDto = AccountDtoMapper.INSTANCE.toDuplicatedCheckDto(joinRequest);
        accountService.CheckDuplicated(checkDto);
        joinRequest.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
        AttachFileDto.Response fileResponse = fileService.uploadFile(joinRequest.getProfileImage());
        joinRequest.getProfileInfo().updateProfileImageId(fileResponse.getId());

        Account newAccount = AccountDtoMapper.INSTANCE.toEntity(joinRequest);
        try {
            Account savedAccount = accountRepository.save(newAccount);
            return AccountDtoMapper.INSTANCE.toResponse(savedAccount);
        } catch (DataIntegrityViolationException e) {
            throw new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_VALUE);
        }
    }

}
