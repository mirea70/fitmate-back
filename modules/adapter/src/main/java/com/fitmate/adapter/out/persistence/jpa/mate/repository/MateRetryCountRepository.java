package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateRetryCountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MateRetryCountRepository extends JpaRepository<MateRetryCountJpaEntity, Long> {

    @Modifying
    @Query("UPDATE MateRetryCountJpaEntity m SET m.retryCount = m.retryCount + 1 WHERE m.mateId = :mateId")
    void incrementRetryCount(Long mateId);
}
