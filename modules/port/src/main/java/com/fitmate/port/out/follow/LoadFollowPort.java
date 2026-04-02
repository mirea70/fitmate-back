package com.fitmate.port.out.follow;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;

import java.util.List;
import java.util.Set;

public interface LoadFollowPort {
    void saveFollowEntity(Account fromAccount, Account toAccount);
    void deleteFollowEntity(Long fromAccountId, Long toAccountId);
    void deleteAllFollowByAccountId(AccountId id);
    Set<Long> getFollowerIds(Long accountId);
    List<FollowDetailResponse> getFollowingsByFrom(Long fromAccountId);
    List<FollowDetailResponse> getFollowersByTarget(Long targetAccountId);
}
