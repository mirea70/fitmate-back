package com.fitmate.account.service;

import com.fitmate.account.dto.AccountDto;
import com.fitmate.account.mapper.AccountDtoMapper;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountDto.JoinResponse join(AccountDto.JoinRequest joinRequest) {
        accountService.CheckDuplicatedByEmail(joinRequest.getPrivateInfo().getEmail());
        Account newAccount = AccountDtoMapper.INSTANCE.toEntity(joinRequest);
        try {
            Account savedAccount = accountRepository.save(newAccount);

            return AccountDtoMapper.INSTANCE.toResponse(savedAccount);
//            return null;
        } catch (DataIntegrityViolationException e) {
            throw new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_VALUE);
        }
    }

}
