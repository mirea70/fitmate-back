package com.fitmate.port.out.sms;

public interface LoadSmsPort {
    void saveValidateCode(String code);
    void sendMessageOne(String to, String content);
    void checkValidateCode(String code);
}
