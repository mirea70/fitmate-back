package com.fitmate.port.out.follow;

import com.fitmate.domain.account.vo.AccountId;

import java.util.List;

public interface LoadFollowPort {
    void saveFollowEntity(Long fromAccountId, Long toAccountId);
    void deleteFollowEntity(Long fromAccountId, Long toAccountId);
    void deleteAllFollowByAccountId(AccountId id);
    List<FollowDetailResponse> getFollowingsByFrom(Long fromAccountId);
    List<FollowDetailResponse> getFollowersByTarget(Long targetAccountId);
}
