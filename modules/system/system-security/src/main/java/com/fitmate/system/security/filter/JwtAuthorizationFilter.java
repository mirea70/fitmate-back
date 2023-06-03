package com.fitmate.system.security.filter;


import com.fitmate.system.security.provider.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }
}
