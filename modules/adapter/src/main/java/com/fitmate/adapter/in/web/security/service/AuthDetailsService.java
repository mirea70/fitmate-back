package com.fitmate.adapter.in.web.security.service;

import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
                () -> new BadCredentialsException("존재하지 않는 로그인 ID입니다."));
        return new AuthDetails(accountEntity);
    }
}
