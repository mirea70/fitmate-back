package com.fitmate.adapter.out.persistence.jpa.follow.repository;

import com.fitmate.adapter.out.persistence.jpa.follow.dto.QFollowDetailJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.follow.dto.FollowDetailJpaResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fitmate.adapter.out.persistence.jpa.account.entity.QAccountJpaEntity.accountJpaEntity;
import static com.fitmate.adapter.out.persistence.jpa.follow.entity.QFollowJpaEntity.followJpaEntity;


@Repository
@RequiredArgsConstructor
public class FollowQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Set<Long> getFollowings(Long fromAccountId) {
        return new HashSet<>(queryFactory
                .select(followJpaEntity.toAccountId)
                .from(followJpaEntity)
                .where(followJpaEntity.fromAccountId.eq(fromAccountId))
                .fetch());
    }

    public Set<Long> getFollowers(Long toAccountId) {
        return new HashSet<>(queryFactory
                .select(followJpaEntity.fromAccountId)
                .from(followJpaEntity)
                .where(followJpaEntity.toAccountId.eq(toAccountId))
                .fetch());
    }

    public List<FollowDetailJpaResponse> getFollowingsByFrom(Long fromAccountId) {
        if(fromAccountId == null) return null;
        return queryFactory
                .select(new QFollowDetailJpaResponse(accountJpaEntity.id, accountJpaEntity.profileImageId, accountJpaEntity.nickName))
                .from(followJpaEntity)
                .leftJoin(accountJpaEntity).on(followJpaEntity.toAccountId.eq(accountJpaEntity.id))
                .fetchJoin()
                .where(followJpaEntity.fromAccountId.eq(fromAccountId))
                .orderBy(accountJpaEntity.nickName.asc())
                .fetch();
    }

    public List<FollowDetailJpaResponse> getFollowersByTarget(Long targetAccountId) {
        if(targetAccountId == null) return null;
        return queryFactory
                .select(new QFollowDetailJpaResponse(accountJpaEntity.id, accountJpaEntity.profileImageId, accountJpaEntity.nickName))
                .from(followJpaEntity)
                .leftJoin(accountJpaEntity).on(followJpaEntity.fromAccountId.eq(accountJpaEntity.id))
                .fetchJoin()
                .where(followJpaEntity.toAccountId.eq(targetAccountId))
                .orderBy(accountJpaEntity.nickName.asc())
                .fetch();
    }
}
