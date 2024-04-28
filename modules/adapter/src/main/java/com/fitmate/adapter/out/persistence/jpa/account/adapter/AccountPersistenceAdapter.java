package com.fitmate.adapter.out.persistence.jpa.account.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.account.mapper.AccountPersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.port.out.account.LoadAccountPort;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort {

    private final AccountRepository accountRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final AccountPersistenceMapper accountPersistenceMapper;


    @Override
    public void saveAccountEntity(Account account) {
        AccountJpaEntity accountEntity = accountPersistenceMapper.domainToEntity(account);
        accountRepository.save(accountEntity);
    }

    @Override
    public boolean checkDuplicated(Long accountId, String nickName, String name, String email, String phone) {
        return accountQueryRepository.checkDuplicated(accountId, nickName, name, email, phone);
    }

    @Override
    public Account loadAccountEntity(AccountId id) {
        AccountJpaEntity accountEntity = accountRepository.getById(id.getValue());
        return accountPersistenceMapper.entityToDomain(accountEntity);
    }

    @Override
    public void deleteAccountEntity(AccountId id) {
        AccountJpaEntity accountEntity = accountRepository.getById(id.getValue());
        accountRepository.delete(accountEntity);
    }
}
