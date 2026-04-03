package com.fitmate.adapter.in.web.account.service;

import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import com.fitmate.port.out.sms.LoadSmsPort;
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
    private final LoadSmsPort loadSmsPort;
    private final BCryptPasswordEncoder passwordEncoder;

    public Map<String, String> findLoginName(String phone) {
        AccountJpaEntity account = accountRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        String loginName = account.getLoginName();
        String masked = maskLoginName(loginName);

        return Map.of("loginName", masked);
    }

    public void requestRecoveryCode(String phone) {
        if (!accountRepository.existsByPhone(phone)) {
            throw new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA);
        }
        String code = generateCode();
        loadSmsPort.saveValidateCode(phone, code);
        loadSmsPort.sendMessageOne(phone, "[FitMate] 인증번호: " + code);
    }

    public void verifyRecoveryCode(String phone, String code) {
        loadSmsPort.checkValidateCode(phone, code);
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

    private String generateCode() {
        StringBuilder code = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
    }
}
