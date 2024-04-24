package com.fitmate.adapter.in.web.security.filter;


import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String uri = request.getRequestURI();
        if(isPassEndPoint(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("인증이나 권한 필요한 주소가 요청 되었습니다.");

        String jwtToken = tokenProvider.resolveToken(request ,"Authorization");

        if(jwtToken != null) {
            TokenProvider.JwtCode accValidResult = tokenProvider.validateToken(jwtToken, "access");

            if(accValidResult == TokenProvider.JwtCode.ACCESS) {
                Authentication authentication = tokenProvider.getAuthentication(jwtToken);
                if(authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }
            } else if (accValidResult == TokenProvider.JwtCode.EXPIRED) {
                String refreshToken = tokenProvider.resolveToken(request, "refresh");
                // refresh Token이 요청헤더에 있다면
                if(refreshToken != null) {
                    // refresh Token을 검증
                    TokenProvider.JwtCode refValidResult = tokenProvider.validateToken(refreshToken, "refresh");
                    // 검증이 통과하면
                    if(refValidResult != TokenProvider.JwtCode.DENIED) {
                        // refresh Token 재발급
                        String newRefreshToken = tokenProvider.updateRefreshToken(refreshToken);
                        if(newRefreshToken != null) {
                            response.addHeader("refresh", "Bearer " + newRefreshToken);
                            // Access Token 재발급
                            Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
                            response.addHeader("Authorization", "Bearer " + tokenProvider.createAccessToken(authDetails.getAccount().getId(),authDetails.getEmail()));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.info("리프레시 토큰 & 액세스 토큰 최신화 완료");
                        }
                        filterChain.doFilter(request, response);
                    }
                } else
                    // refresh Token이 요청헤더에 없다면
                    // 클라이언트에 Access Token이 만료되었음을 알리고, refresh Token을 요청헤더에 달라고 요청(예외 처리)
                    throw new TokenExpiredException("토큰 만료", Instant.now());
            }
        } else {
            log.info("유효한 토큰을 찾지 못하였습니다, uri : {}", request.getRequestURI());
            filterChain.doFilter(request, response);
        }
    }

    private boolean isPassEndPoint(String path) {
        if(path.equals("/api/account/join") || path.equals("/check")) {
            return true;
        }
        return false;
    }
}
