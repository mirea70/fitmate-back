package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fitmate.adapter.out.persistence.jpa.account.entity.QAccountJpaEntity.accountJpaEntity;
import static com.fitmate.adapter.out.persistence.jpa.mate.entity.QMateJpaEntity.mateJpaEntity;

@Repository
@RequiredArgsConstructor
public class MateQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<MateSimpleJpaResponse> readList(Long lastMatingId, int limit) {
        return  queryFactory
                .select(new QMateSimpleJpaResponse(
                        mateJpaEntity.id,
                        mateJpaEntity.fitCategory,
                        mateJpaEntity.title,
                        mateJpaEntity.mateAt,
                        mateJpaEntity.fitPlaceName,
                        mateJpaEntity.fitPlaceAddress,
                        mateJpaEntity.gatherType,
                        mateJpaEntity.permitGender,
                        mateJpaEntity.permitMaxAge,
                        mateJpaEntity.permitMinAge,
                        mateJpaEntity.permitPeopleCnt,
                        mateJpaEntity.waitingAccountIds,
                        mateJpaEntity.approvedAccountIds
                        ))
                .from(mateJpaEntity)
                .orderBy(mateJpaEntity.createdAt.desc())
                .where(afterLastMatingId(lastMatingId))
                .limit(limit)
                .fetch();
    }

    private BooleanExpression afterLastMatingId(Long lastMatingId) {
        return lastMatingId != null ? mateJpaEntity.id.gt(lastMatingId) : null;
    }

    public MateQuestionJpaResponse readQuestion(Long mateId) {
        return queryFactory
                .select(new QMateQuestionJpaResponse(
                        accountJpaEntity.profileImageId,
                        accountJpaEntity.nickName,
                        mateJpaEntity.applyQuestion))
                .from(mateJpaEntity)
                .innerJoin(accountJpaEntity).on(mateJpaEntity.writerId.eq(accountJpaEntity.id))
                .where(mateJpaEntity.id.eq(mateId))
                .fetchOne();
    }

    public List<MateApplySimpleJpaResponse> getMyMateRequests(List<Long> matingIds) {
        return null;
//        return queryFactory
//                .select(new QMateRequestSimpleJpaResponse(
//                        mateJpaEntity.id,
//                        mateJpaEntity.introImageIds,
//                        mateJpaEntity.title,
//                        mateJpaEntity.mateAt,
//                        mateJpaEntity.fitPlaceName,
//                        mateJpaEntity.fitPlaceAddress,
//                        mateJpaEntity.permitPeopleCnt,
//                        mateJpaEntity.approvedAccountIds,
//                        mateJpaEntity.totalFee,
//                        mateRequestJpaEntity.createdAt))
//                .from(mateJpaEntity)
//                .innerJoin(mateRequestJpaEntity).on(mateJpaEntity.id.eq(mateRequestJpaEntity.mateId))
//                .where(mateJpaEntity.id.in(matingIds))
//                .orderBy(mateRequestJpaEntity.createdAt.desc())
//                .fetch();
    }
}
