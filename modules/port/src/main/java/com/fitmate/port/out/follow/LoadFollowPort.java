package com.fitmate.port.out.follow;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;

import java.util.List;

public interface LoadFollowPort {
    void saveFollowEntity(Account fromAccount, Account toAccount);
    void deleteFollowEntity(Long fromAccountId, Long toAccountId);
    void deleteAllFollowByAccountId(AccountId id);
    List<FollowDetailResponse> getFollowingsByFrom(Long fromAccountId);
    List<FollowDetailResponse> getFollowersByTarget(Long targetAccountId);
}
