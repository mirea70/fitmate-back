package com.fitmate.domain.account.service;

import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    public void CheckDuplicatedByEmail(String email) throws AccountDuplicatedException {
        accountRepository.findByPrivateInfoEmail(email)
                .ifPresent(m -> {throw new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);});
    }
}
