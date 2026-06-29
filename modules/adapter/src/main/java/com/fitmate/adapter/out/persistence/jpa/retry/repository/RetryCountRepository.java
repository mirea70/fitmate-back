package com.fitmate.adapter.out.persistence.jpa.retry.repository;

import com.fitmate.adapter.out.persistence.jpa.retry.entity.RetryCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryDomain;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetryCountRepository extends JpaRepository<RetryCountJpaEntity, Long> {

    boolean existsByDomainAndTargetIdAndType(RetryDomain domain, Long targetId, RetryType type);
}
