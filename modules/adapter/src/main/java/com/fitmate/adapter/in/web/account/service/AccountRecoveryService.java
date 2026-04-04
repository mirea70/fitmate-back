package com.fitmate.adapter.in.web.account.service;

import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountRecoveryService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Map<String, String> findLoginName(String phone) {
        AccountJpaEntity account = accountRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        String loginName = account.getLoginName();
        String masked = maskLoginName(loginName);

        return Map.of("loginName", masked);
    }

    public void checkPhoneExists(String phone) {
        if (!accountRepository.existsByPhone(phone)) {
            throw new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA);
        }
    }

    public void resetPassword(String phone, String newPassword) {
        AccountJpaEntity account = accountRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        account.syncPassword(passwordEncoder.encode(newPassword));
    }

    private String maskLoginName(String loginName) {
        if (loginName.startsWith("kakao_")) return "카카오 로그인 계정";
        if (loginName.length() <= 3) return loginName.charAt(0) + "**";
        return loginName.substring(0, 3) + "*".repeat(loginName.length() - 3);
    }

}
