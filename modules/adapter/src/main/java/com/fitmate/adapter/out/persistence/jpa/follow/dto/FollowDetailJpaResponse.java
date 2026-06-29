package com.fitmate.adapter.out.persistence.jpa.follow.dto;

import com.querydsl.core.annotations.QueryProjection;

public record FollowDetailJpaResponse(
        Long accountId,
        Long profileImageId,
        String nickName
) {
    @QueryProjection
    public FollowDetailJpaResponse {
    }
}
