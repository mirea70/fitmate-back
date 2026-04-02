package com.fitmate.adapter.in.web.security.service;

import com.fitmate.adapter.in.web.security.dto.KakaoLoginResponse;
import com.fitmate.adapter.in.web.security.dto.KakaoRegisterRequest;
import com.fitmate.adapter.in.web.security.dto.KakaoTokenResponse;
import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoAuthService {

    private static final String KAKAO_LOGIN_NAME_PREFIX = "kakao_";
    private static final String KAKAO_DEFAULT_EMAIL_SUFFIX = "@kakao.fitmate";

    private final AccountRepository accountRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.userInfoUrl}")
    private String kakaoUserInfoUrl;

    public KakaoLoginResponse kakaoLogin(String kakaoAccessToken) {
        KakaoUserInfo kakaoUserInfo = fetchKakaoUserInfo(kakaoAccessToken);
        String loginName = KAKAO_LOGIN_NAME_PREFIX + kakaoUserInfo.kakaoId;

        Optional<AccountJpaEntity> existingAccount = accountRepository.findByLoginName(loginName);
        if (existingAccount.isPresent()) {
            return loginExistingUser(existingAccount.get());
        }

        return KakaoLoginResponse.newUser(
                kakaoUserInfo.nickName != null ? kakaoUserInfo.nickName : "",
                kakaoUserInfo.email != null ? kakaoUserInfo.email : ""
        );
    }

    public KakaoTokenResponse kakaoRegister(KakaoRegisterRequest request) {
        KakaoUserInfo kakaoUserInfo = fetchKakaoUserInfo(request.getAccessToken());
        String loginName = KAKAO_LOGIN_NAME_PREFIX + kakaoUserInfo.kakaoId;

        if (accountRepository.findByLoginName(loginName).isPresent()) {
            throw new IllegalStateException("이미 가입된 카카오 계정입니다.");
        }

        AccountJpaEntity saved = createAccount(loginName, request);
        return issueTokens(saved);
    }

    private KakaoLoginResponse loginExistingUser(AccountJpaEntity account) {
        String accessToken = tokenProvider.createAccessToken(account.getId(), account.getEmail());
        String refreshToken = tokenProvider.renewalRefreshToken(account.getId(), account.getEmail());
        return KakaoLoginResponse.existingUser(accessToken, refreshToken);
    }

    private KakaoTokenResponse issueTokens(AccountJpaEntity account) {
        String accessToken = tokenProvider.createAccessToken(account.getId(), account.getEmail());
        String refreshToken = tokenProvider.renewalRefreshToken(account.getId(), account.getEmail());
        return new KakaoTokenResponse(accessToken, refreshToken);
    }

    private AccountJpaEntity createAccount(String loginName, KakaoRegisterRequest request) {
        String rawPassword = "Kakao@" + UUID.randomUUID().toString().substring(0, 8) + "1!";
        String email = request.getEmail() != null && !request.getEmail().isBlank()
                ? request.getEmail()
                : loginName + KAKAO_DEFAULT_EMAIL_SUFFIX;
        LocalDate birthDate = request.getBirthDate() != null ? LocalDate.parse(request.getBirthDate()) : null;

        AccountJpaEntity newAccount = AccountJpaEntity.builder()
                .loginName(loginName)
                .password(passwordEncoder.encode(rawPassword))
                .nickName(request.getNickName())
                .introduction("")
                .name(request.getName())
                .phone(request.getPhone())
                .email(email)
                .birthDate(birthDate)
                .gender(request.getGender())
                .role("USER")
                .build();

        return accountRepository.save(newAccount);
    }

    @SuppressWarnings("unchecked")
    private KakaoUserInfo fetchKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> response = restTemplate.exchange(
                kakaoUserInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new IllegalStateException("카카오 사용자 정보 조회에 실패했습니다.");
        }

        Map<String, Object> body = response.getBody();
        log.info("카카오 사용자 정보: {}", body);

        Long kakaoId = ((Number) body.get("id")).longValue();
        String nickName = extractNestedValue(body, "kakao_account", "profile", "nickname");
        if (nickName == null) {
            nickName = extractNestedValue(body, "properties", "nickname");
        }
        String email = extractNestedValue(body, "kakao_account", "email");

        return new KakaoUserInfo(kakaoId, nickName, email);
    }

    @SuppressWarnings("unchecked")
    private String extractNestedValue(Map<String, Object> root, String... keys) {
        Object current = root;
        for (int i = 0; i < keys.length; i++) {
            if (!(current instanceof Map)) return null;
            current = ((Map<String, Object>) current).get(keys[i]);
            if (current == null) return null;
        }
        return current instanceof String ? (String) current : null;
    }

    private record KakaoUserInfo(Long kakaoId, String nickName, String email) {}
}
