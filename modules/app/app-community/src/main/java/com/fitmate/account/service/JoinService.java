package com.fitmate.account.service;

import com.fitmate.account.dto.AccountDto;
import com.fitmate.account.mapper.AccountDtoMapper;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
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

    private final AccountRepository accountRepository;

    public Account join(AccountDto.JoinRequest joinRequest) {
        final Account findAccount = accountRepository.findByPrivateInfoEmail(joinRequest.getPrivateInfo().getEmail());
        if(findAccount != null) {
            throw new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);
        }
        Account newAccount = AccountDtoMapper.INSTANCE.toEntity(joinRequest);
        try {
            return accountRepository.save(newAccount);
        } catch (DataIntegrityViolationException e) {
            throw new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_VALUE);
        }
    }

}
