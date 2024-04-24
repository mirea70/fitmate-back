package com.fitmate.adapter.in.web.security.service;

import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public AuthDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        AccountJpaEntity accountEntity = accountRepository.findByLoginName(loginName).orElseThrow(
                () -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        return new AuthDetails(accountEntity);
    }
}
