package com.fitmate.usecase.sms;

import com.fitmate.port.in.sms.usecase.SmsUseCasePort;
import com.fitmate.port.out.sms.LoadSmsPort;
import com.fitmate.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class SmsUseCase implements SmsUseCasePort {

    private final LoadSmsPort loadSmsPort;

    @Override
    public void requestValidateCode(String to) {
        String code = createValidateCode(8);
        loadSmsPort.saveValidateCode(code);

        String content = "인증번호 : " + code;
        loadSmsPort.sendMessageOne(to, content);
    }

    private String createValidateCode(int len) {
        return RandomStringUtils.randomAlphanumeric(len);
    }

    @Override
    public void checkValidateCode(String inputCode) {
        loadSmsPort.checkValidateCode(inputCode);
    }
}
