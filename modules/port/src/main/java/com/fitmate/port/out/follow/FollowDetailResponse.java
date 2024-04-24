package com.fitmate.port.out.follow;

import lombok.Getter;

@Getter
public class FollowDetailResponse {
    private final Long accountId;
    private final Long profileImageId;
    private final String nickName;

    public FollowDetailResponse(Long accountId, Long profileImageId, String nickName) {
        this.accountId = accountId;
        this.profileImageId = profileImageId;
        this.nickName = nickName;
    }
}
