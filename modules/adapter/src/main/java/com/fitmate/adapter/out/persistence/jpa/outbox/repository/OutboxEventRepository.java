package com.fitmate.adapter.out.persistence.jpa.outbox.repository;

import com.fitmate.adapter.out.persistence.jpa.outbox.OutboxEventStatus;
import com.fitmate.adapter.out.persistence.jpa.outbox.entity.OutboxEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventJpaEntity, Long> {

    List<OutboxEventJpaEntity> findTop50ByStatusInAndRetryCountLessThanOrderByCreatedAtAsc(
            Collection<OutboxEventStatus> statuses,
            int retryCount
    );

    List<OutboxEventJpaEntity> findAllByStatusInAndRetryCountLessThanAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
            Collection<OutboxEventStatus> statuses,
            int retryCount,
            LocalDateTime createdAt,
            Pageable pageable
    );
}
