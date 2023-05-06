package com.fitmate.account.service;

import com.fitmate.account.dto.AccountDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account join(AccountDto.JoinRequest joinRequest) {
        final Account findAccount = accountRepository.findByPrivateInfoEmail(joinRequest.getEmail());
        if(findAccount != null) {
            throw new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);
        }
        return null;
    }
}
