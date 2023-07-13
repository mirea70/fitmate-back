package com.fitmate.domain.mating.domain.repository;

import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.domain.entity.QMating;
import com.fitmate.domain.mating.dto.MatingReadResponseDto;
import com.fitmate.domain.mating.dto.QMatingReadResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fitmate.domain.mating.domain.entity.QMating.mating;


@Repository
@RequiredArgsConstructor
public class MatingReadRepository {
    private final JPAQueryFactory queryFactory;

    public List<Mating> readList(Long lastMatingId, int limit) {

        return queryFactory
                .select(mating)
                .from(mating)
//                .orderBy(mating.createdAt.desc())
//                .where(afterLastMatingId(lastMatingId))
//                .limit(limit)
                .fetch();
    }

    private BooleanExpression afterLastMatingId(Long lastMatingId) {
        return lastMatingId != null ? mating.id.lt(lastMatingId) : null;
    }
}
