package com.fitmate.system.security.service;

import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import com.fitmate.system.security.dto.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public AuthDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        Account accountEntity = accountRepository.findByLoginName(loginName).orElseThrow(
                () -> new NotFoundException(NotFoundErrorResult.NOT_FOUNT_ACCOUNT_DATA));
        return new AuthDetails(accountEntity);
    }
}
