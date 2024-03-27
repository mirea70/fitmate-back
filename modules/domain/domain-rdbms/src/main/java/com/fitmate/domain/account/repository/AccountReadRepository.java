package com.fitmate.domain.account.repository;

import com.fitmate.domain.account.dto.FollowDetailDto;
import com.fitmate.domain.account.dto.QFollowDetailDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.fitmate.domain.account.entity.QAccount.account;

@Repository
@RequiredArgsConstructor
public class AccountReadRepository {
    private final JPAQueryFactory queryFactory;

    public List<FollowDetailDto> findAllInId(Set<Long> accountIds) {

        if(accountIds == null || accountIds.isEmpty()) return null;
        return queryFactory
                .select(new QFollowDetailDto(account.id, account.profileInfo.profileImageId, account.profileInfo.nickName))
                .from(account)
                .where(account.id.in(accountIds))
                .fetch();
    }
}
