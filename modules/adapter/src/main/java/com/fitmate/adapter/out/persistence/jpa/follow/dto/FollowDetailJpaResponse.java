package com.fitmate.adapter.out.persistence.jpa.follow.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FollowDetailJpaResponse {
    private final Long accountId;
    private final Long profileImageId;
    private final String nickName;

    @QueryProjection
    public FollowDetailJpaResponse(Long accountId, Long profileImageId, String nickName) {
        this.accountId = accountId;
        this.profileImageId = profileImageId;
        this.nickName = nickName;
    }
}
