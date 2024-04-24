package com.fitmate.adapter.out.persistence.jpa.account.repository;

import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {

    @NonNull
    default AccountJpaEntity getById(@NonNull Long id) {
        return this.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
    }

    @NonNull
    default AccountJpaEntity getByEmail(@NonNull String email) {
        return this.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
    }

    Optional<AccountJpaEntity> findByEmail(String email);

    @NonNull
    default AccountJpaEntity getByLoginName(@NonNull String loginName) {
        return this.findByLoginName(loginName)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
    }

    Optional<AccountJpaEntity> findByLoginName(String loginName);
}
