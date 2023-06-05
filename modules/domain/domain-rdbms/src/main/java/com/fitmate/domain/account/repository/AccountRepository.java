package com.fitmate.domain.account.repository;

import com.fitmate.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByPrivateInfoEmail(String email);

    Optional<Account> findByLoginName(String loginName);

    @Query(value = "SELECT COUNT(a) FROM Account a " +
            "WHERE a.privateInfo.email = :email OR a.privateInfo.name = :name OR " +
            "a.privateInfo.phone = :phone OR a.profileInfo.nickName = :nickName")
    int checkDuplicatedCount(@Param("name") String name, @Param("email") String email,
                        @Param("phone") String phone, @Param("nickName") String nickName);
}
