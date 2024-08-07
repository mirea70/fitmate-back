package com.fitmate.adapter.in.web.security.handler;

import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import com.fitmate.adapter.out.persistence.redis.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rawToken = tokenProvider.resolveToken(request, "Authorization");
        Authentication auth = tokenProvider.getAuthentication(rawToken);

        AuthDetails authDetails = (AuthDetails) auth.getPrincipal();
        String email = authDetails.getEmail();
        refreshTokenRepository.deleteById(email);
        log.info("해당 유저의 refresh 토큰이 삭제되었습니다. email : {}", email);
    }
}
