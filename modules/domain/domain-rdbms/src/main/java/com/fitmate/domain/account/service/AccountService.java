package com.fitmate.domain.account.service;

import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.mapper.AccountDataDtoMapper;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.DuplicatedErrorResult;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountDataDto.Response validateFindById(Long id) {
        Account findAccount = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        return AccountDataDtoMapper.INSTANCE.toResponse(findAccount);
    }

    public void CheckDuplicatedByEmail(String email) throws DuplicatedException {
        accountRepository.findByPrivateInfoEmail(email)
                .ifPresent(m -> {throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_ACCOUNT_JOIN);});
    }

    public void CheckDuplicated(AccountDuplicateCheckDto checkDto) throws DuplicatedException {
        int duplicatedCount = accountRepository.checkDuplicatedCount(checkDto.getName(), checkDto.getEmail(),
                                                                    checkDto.getPhone(), checkDto.getNickName());
        if(duplicatedCount > 1) throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_ACCOUNT_JOIN);
    }
}
