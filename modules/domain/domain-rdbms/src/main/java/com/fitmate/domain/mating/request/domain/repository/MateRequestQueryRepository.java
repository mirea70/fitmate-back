package com.fitmate.domain.mating.request.domain.repository;

import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Set;

import static com.fitmate.domain.mating.request.domain.entity.QMateRequest.mateRequest;

@Repository
@RequiredArgsConstructor
public class MateRequestQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    public void approveAccountIds(Long matingId, Set<Long> accountIds) {
        queryFactory
                .update(mateRequest)
                .set(mateRequest.approveStatus, MateRequest.ApproveStatus.APPROVE)
                .where(mateRequest.matingId.eq(matingId), mateRequest.accountId.in(accountIds))
                .execute();
    }
}
