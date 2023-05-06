package com.fitmate.domain.account.repository;

import com.fitmate.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByPrivateInfoEmail(String email);
}
