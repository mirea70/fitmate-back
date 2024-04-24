package com.fitmate.adapter.in.web.security.config;

import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.in.web.security.service.AuthDetailsService;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthDetailsService authDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginName = authentication.getName();
        String password = (String) authentication.getCredentials();

        AuthDetails authDetails = authDetailsService.loadUserByUsername(loginName);
        if(authDetails == null) throw new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA);
        if(!passwordEncoder.matches(password, authDetails.getPassword())) {
            throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
        }

        return new UsernamePasswordAuthenticationToken(authDetails, password, authDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
