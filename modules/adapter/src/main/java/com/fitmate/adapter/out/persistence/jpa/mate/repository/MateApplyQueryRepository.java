package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fitmate.adapter.out.persistence.jpa.mate.entity.QMateApplyJpaEntity.mateApplyJpaEntity;

@Repository
@RequiredArgsConstructor
public class MateApplyQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Long> getMateIdsFromMateRequest(Long applierId, String approveStatus) {
        return queryFactory
                .select(mateApplyJpaEntity.mateId)
                .from(mateApplyJpaEntity)
                .where(mateApplyJpaEntity.applierId.eq(applierId).and(mateApplyJpaEntity.approveStatus.eq(approveStatus)))
                .fetch();
    }
}
