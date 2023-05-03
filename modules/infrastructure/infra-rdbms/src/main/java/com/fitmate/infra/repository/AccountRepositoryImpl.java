package com.fitmate.infra.repository;

import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final EntityManager entityManager;

    @Override
    public Account findById(Long accountId) {
        return entityManager.find(Account.class, accountId);
    }

    @Override
    public void save(Account account) {
        entityManager.persist(account);
    }

}
