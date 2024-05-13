package com.fitmate.port.in.sms.usecase;

public interface SmsUseCasePort {
    void requestValidateCode(String phone);
    void checkValidateCode(String inputCode);
}
