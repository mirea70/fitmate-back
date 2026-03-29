package com.fitmate.port.out.sms;

public interface LoadSmsPort {
    void saveValidateCode(String phone, String code);
    void sendMessageOne(String to, String content);
    void checkValidateCode(String phone, String code);
}
