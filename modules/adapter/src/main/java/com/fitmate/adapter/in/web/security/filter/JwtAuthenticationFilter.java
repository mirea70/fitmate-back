package com.fitmate.adapter.in.web.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(String defaultFilterProcessesUrl,
                                      AuthenticationManager authenticationManager,
                                      TokenProvider tokenProvider,
                                      AuthenticationFailureHandler failureHandler) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        logger.info("로그인 시도");

        try {
            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
            String loginName = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginName, password);
            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new IllegalArgumentException("요청 파라미터에 문제가 있습니다.");
        }
    }
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException, ServletException {
            logger.info(("로그인 성공 유저 처리"));

            AuthDetails authDetails = (AuthDetails) authResult.getPrincipal();

            String accessToken = tokenProvider.createAccessToken(authDetails.getAccount().getId(), authDetails.getEmail());
            String refreshToken = tokenProvider.renewalRefreshToken(authDetails.getAccount().getId(), authDetails.getEmail());
            response.addHeader("access", "Bearer " + accessToken);
            response.addHeader("refresh", "Bearer " + refreshToken);
        }
}
