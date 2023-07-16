package com.fitmate.domain.mating.domain.repository;

import com.fitmate.domain.mating.dto.MatingQuestionDto;
import com.fitmate.domain.mating.dto.MatingReadResponseDto;
import com.fitmate.domain.mating.dto.QMatingQuestionDto_Response;
import com.fitmate.domain.mating.dto.QMatingReadResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fitmate.domain.account.entity.QAccount.account;
import static com.fitmate.domain.mating.domain.entity.QMating.mating;


@Repository
@RequiredArgsConstructor
public class MatingReadRepository {
    private final JPAQueryFactory queryFactory;

    public List<MatingReadResponseDto> readList(Long lastMatingId, int limit) {

        return  queryFactory
                .select(new QMatingReadResponseDto(mating.id, mating.fitCategory, mating.title,
                        mating.mateAt, mating.fitPlace.name, mating.fitPlace.address, mating.gatherType,
                        mating.permitGender, mating.permitAges.max, mating.permitAges.min, mating.permitPeopleCnt))
                .from(mating)
                .orderBy(mating.createdAt.desc())
                .where(afterLastMatingId(lastMatingId))
                .limit(limit)
                .fetch();
    }

    private BooleanExpression afterLastMatingId(Long lastMatingId) {
        return lastMatingId != null ? mating.id.lt(lastMatingId) : null;
    }

    public MatingQuestionDto.Response readQuestion(Long matingId) {
        return queryFactory
                .select(new QMatingQuestionDto_Response(account.profileInfo.profileImageId, account.profileInfo.nickName, mating.comeQuestion))
                .from(mating)
                .innerJoin(account).on(mating.writerId.eq(account.id))
                .fetchOne();
    }
}
