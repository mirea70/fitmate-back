package com.fitmate.domain.account.repository;

import com.fitmate.domain.account.entity.Account;

public interface AccountRepository {
    Account findById(Long accountId);

    void save(Account account);
}
