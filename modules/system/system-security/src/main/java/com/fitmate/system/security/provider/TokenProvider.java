package com.fitmate.system.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.redis.entity.RefreshToken;
import com.fitmate.domain.redis.repository.RefreshTokenRepository;
import com.fitmate.system.security.dto.AuthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private final String AUTHORITIES_KEY = "auth";
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;

    @Value(value = "{jwt.secretKey}")
    private String accessKey;

    private final String refreshKey = "refresh " + accessKey;

    public String createAccessToken(Long accountId, String email) {

        return JWT.create()
                .withSubject("feetMate JWT Access Token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 60)))
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 30)))
//                .withExpiresAt(new Date(System.currentTimeMillis() + (2 * 1000 )))
                .withClaim("accountId", accountId)
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(accessKey));
    }

    private String createRefreshToken(Long accountId, String email) {

        return JWT.create()
                .withSubject("feetMate JWT Refresh Token")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 60 * 24 * 14)))
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 1000 * 30)))
//                .withExpiresAt(new Date(System.currentTimeMillis() + (2 * 1000 )))
                .withClaim("accountId", accountId)
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(refreshKey));
    }

    @Transactional
    public String renewalRefreshToken(Long accountId, String email) {
        String newRefreshToken = createRefreshToken(accountId, email);
        // 기존 토큰이 있다면 바꿔주고, 없다면 토큰을 만들어준다.
        refreshTokenRepository.findById(email)
                .ifPresentOrElse(
                        r -> {r.changeToken(newRefreshToken);
                            log.info("기존 리프레시 토큰 변환");
                            refreshTokenRepository.save(r);
                            },
                        () -> {
                            RefreshToken toSaveToken = RefreshToken.builder()
                                    .token(newRefreshToken)
                                    .email(email)
                                    .build();
                            log.info("새로운 리프레시 토큰 저장 | member's email : {}, token : {}", toSaveToken.getEmail(), toSaveToken.getToken() );
                            refreshTokenRepository.save(toSaveToken);
                        }
                );

        return newRefreshToken;
    }

    @Transactional
    public String updateRefreshToken(String refreshToken) throws RuntimeException {
        // refresh Token을 DB에 저장된 토큰과 비교
        Authentication authentication = getAuthentication(refreshToken);
        AuthDetails authdetails = (AuthDetails) authentication.getPrincipal();
        RefreshToken findRefreshToken = refreshTokenRepository.findById(authdetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("email : " + authentication.getName() + " was not found"));

        // 토큰이 일치한다면
        if(findRefreshToken.getToken().equals(refreshToken)) {
            // 새로운 토큰 생성
            String newRefreshToken = createRefreshToken(authdetails.getAccount().getId(), authdetails.getEmail());
            findRefreshToken.changeToken(newRefreshToken);
            return newRefreshToken;
        } else {
            log.info("refresh Token이 일치하지 않습니다.");
            return null;
        }
    }




    public Authentication getAuthentication(String token) {

        String email = JWT.decode(token).getClaim("email").asString();

        if(email != null) {
            Account accountEntity = accountRepository.findByPrivateInfoEmail(email).orElseThrow(
                    () -> new UsernameNotFoundException(email + " : 데이터베이스에서 찾을 수 없습니다.")
            );

            AuthDetails authDetails = new AuthDetails(accountEntity);

            return new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities());
        } else {
            return null;
        }
    }

    public JwtCode validateToken(String token, String tokenType) {
        String key = tokenType.equals("access") ? accessKey : refreshKey;

        try {
            JWT.require(Algorithm.HMAC512(key)).build().verify(token);
            return JwtCode.ACCESS;
        } catch (TokenExpiredException e) {
            log.info("만료된 토큰입니다.");
            return JwtCode.EXPIRED;
        } catch (JWTVerificationException e) {
            log.info("토큰 검증에 실패하였습니다.");
            return JwtCode.DENIED;
        }
    }

    public String resolveToken(HttpServletRequest request, String header) {
        String jwtHeader = request.getHeader(header);

        if(jwtHeader != null && jwtHeader.startsWith("Bearer")) {
            return jwtHeader.replace("Bearer ", "");
        }
        return null;
    }

    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED;
    }
}
