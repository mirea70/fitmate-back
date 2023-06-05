package com.fitmate.system.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.system.security.dto.AuthDetails;
import com.fitmate.system.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        logger.info("로그인 시도");

        try {
//            ObjectMapper om = new ObjectMapper();
//            JsonNode jsonNode = om.readTree(request.getInputStream());
//            String loginName = jsonNode.get("loginName").asText();
//            String password = jsonNode.get("password").asText();
            String loginName = request.getParameter("loginName");
            String password = request.getParameter("password");


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginName, password);
            return authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
        @Override
        protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authResult) throws IOException, ServletException {
            logger.info(("로그인 성공 유저 처리"));

            AuthDetails authDetails = (AuthDetails) authResult.getPrincipal();

            String accessToken = tokenProvider.createAccessToken(authDetails.getAccount().getId(), authDetails.getEmail());
            String refreshToken = tokenProvider.renewalRefreshToken(authDetails.getAccount().getId(), authDetails.getEmail());
            response.addHeader("Authorization", "Bearer " + accessToken);
            response.addHeader("refresh", "Bearer " + refreshToken);
        }
}
