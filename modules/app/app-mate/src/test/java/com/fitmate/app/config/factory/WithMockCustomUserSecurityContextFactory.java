package com.fitmate.app.config.factory;

import com.fitmate.app.config.annotation.WithMockCustomUser;
import com.fitmate.system.security.dto.AuthDetails;
import com.fitmate.system.security.service.AuthDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Configuration
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final AuthDetailsService authDetailsService;

    @Autowired
    public WithMockCustomUserSecurityContextFactory(AuthDetailsService authDetailsService) {
        this.authDetailsService = authDetailsService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

        String loginName = customUser.username();
        AuthDetails authDetails = authDetailsService.loadUserByUsername(loginName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(authDetails, authDetails.getPassword(), authDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
