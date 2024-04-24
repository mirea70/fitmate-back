package com.fitmate.port.out.account;

import com.fitmate.domain.account.aggregate.Account;
import com.fitmate.domain.account.vo.AccountId;

public interface LoadAccountPort {
    Account loadAccountEntity(AccountId id);
    void saveAccountEntity(Account account);
    void deleteAccountEntity(AccountId id);
    boolean checkDuplicated(Long accountId, String nickName, String name, String email, String phone);
}
