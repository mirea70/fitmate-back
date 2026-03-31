package com.fitmate.usecase.account;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.port.in.account.usecase.AccountPasswordUseCasePort;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.port.out.sms.LoadSmsPort;
import com.fitmate.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class AccountPasswordUseCase implements AccountPasswordUseCasePort {

    private final LoadAccountPort loadAccountPort;
    private final LoadSmsPort loadSmsPort;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void validateCurrentPassword(Long accountId, String inputPassword) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        if(!isCorrectPassword(inputPassword, account.getPassword().getValue()))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_CURRENT_PASSWORD);
    }

    private boolean isCorrectPassword(String input, String current) {
        return passwordEncoder.matches(input, current);
    }

    @Override
    public void requestValidateCode(Long accountId) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        String code = createValidateCode(8);
        String phone = account.getPrivateInfo().getPhone();
        loadSmsPort.saveValidateCode(phone, code);

        String content = "인증번호 : " + code;
        loadSmsPort.sendMessageOne(phone, content);
    }

    private String createValidateCode(int len) {
        return RandomStringUtils.randomAlphanumeric(len);
    }

    @Override
    public void checkValidateCode(String phone, String inputCode) {
        loadSmsPort.checkValidateCode(phone, inputCode);
    }

    @Override
    public void saveNewPassword(Long accountId, String newPassword) {
        Loaded<Account> loadedAccount = loadAccountPort.loadAccount(new AccountId(accountId));
        loadedAccount.update(account -> account.changePassword(passwordEncoder.encode(newPassword)));
    }
}
