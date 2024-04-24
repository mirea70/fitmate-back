package com.fitmate.adapter.out.persistence.jpa.follow.repository;

import com.fitmate.adapter.out.persistence.jpa.follow.entity.FollowJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowJpaEntity, Long> {
    void deleteByFromAccountIdAndToAccountId(Long fromAccountId, Long toAccountId);
    void deleteAllByFromAccountIdOrToAccountId(Long fromAccountId, Long toAccountId);
}
