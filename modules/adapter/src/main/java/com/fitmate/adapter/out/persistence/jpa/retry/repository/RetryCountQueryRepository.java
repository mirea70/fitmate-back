package com.fitmate.adapter.out.persistence.jpa.retry.repository;

import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryDomain;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.fitmate.adapter.out.persistence.jpa.retry.entity.QRetryCountJpaEntity.retryCountJpaEntity;

@Repository
@RequiredArgsConstructor
public class RetryCountQueryRepository {

    private final JPAQueryFactory queryFactory;

    public void incrementRetryCount(RetryDomain domain, Long targetId, RetryType type) {
        queryFactory
                .update(retryCountJpaEntity)
                .set(retryCountJpaEntity.retryCount, retryCountJpaEntity.retryCount.add(1))
                .where(
                        retryCountJpaEntity.domain.eq(domain),
                        retryCountJpaEntity.targetId.eq(targetId),
                        retryCountJpaEntity.type.eq(type)
                )
                .execute();
    }
}
