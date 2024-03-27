package com.fitmate.app.mate.account.service;


import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.redis.entity.ValidateCode;
import com.fitmate.domain.redis.repository.ValidateCodeRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.exception.NotMatchException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import com.fitmate.exceptions.result.NotMatchErrorResult;
import com.fitmate.system.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountValidateService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SmsUtil smsUtil;
    private final ValidateCodeRepository validateCodeRepository;

    public void validateCurrentPassword(Long accountId, String inputPassword) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        if(!isCorrectPassword(inputPassword, account.getPassword().getValue()))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_CURRENT_PASSWORD);
    }

    private boolean isCorrectPassword(String input, String current) {
        return passwordEncoder.matches(input, current);
    }

    public void requestValidateCode(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        String validateCodeItem = createValidateCode(8);
        saveValidateCode(validateCodeItem);
        smsUtil.sendOne(account.getPrivateInfo().getPhone(), "인증번호 : " + validateCodeItem);
    }

    private String createValidateCode(int len) {
        return RandomStringUtils.randomAlphanumeric(len);
    }

    private void saveValidateCode(String validateCodeItem) {
        ValidateCode validateCode = ValidateCode.builder()
                .code(validateCodeItem)
                .build();
        validateCodeRepository.save(validateCode);
    }

    public void checkValidateCode(String inputCode) {
        ValidateCode validateCode = validateCodeRepository.findById(inputCode)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_VALIDATE_DATA));
    }

    public void saveNewPassword(Long accountId, String newPassword) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        account.getPassword().update(passwordEncoder.encode(newPassword));
    }
}
