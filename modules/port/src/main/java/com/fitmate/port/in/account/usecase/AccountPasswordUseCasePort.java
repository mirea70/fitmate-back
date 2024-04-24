package com.fitmate.port.in.account.usecase;

public interface AccountPasswordUseCasePort {
    void validateCurrentPassword(Long accountId, String inputPassword);
    void requestValidateCode(Long accountId);
    void checkValidateCode(String inputCode);
    void saveNewPassword(Long accountId, String newPassword);
}
