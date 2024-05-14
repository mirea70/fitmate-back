package com.fitmate.port.out.account;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;

public interface LoadAccountPort {
    Account loadAccountEntity(AccountId id);
    void saveAccountEntity(Account account);
    void deleteAccountEntity(AccountId id);
    boolean checkDuplicated(Long accountId, String nickName, String name, String email, String phone);
    boolean checkDuplicatedLoginName(String loginName);
    boolean checkDuplicatedPhone(String phone);
}
