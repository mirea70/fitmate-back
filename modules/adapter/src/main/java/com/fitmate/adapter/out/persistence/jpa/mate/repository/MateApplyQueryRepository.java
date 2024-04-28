package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MateApplyQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Long> getMateIdsFromMateRequest(Long applierId, String approveStatus) {
        return null;
//        return queryFactory
//                .select(mateRequestJpaEntity.mateId)
//                .from(mateRequestJpaEntity)
//                .where(mateRequestJpaEntity.applierId.eq(applierId).and(mateRequestJpaEntity.approveStatus.eq(approveStatus)))
//                .fetch();
    }
}
