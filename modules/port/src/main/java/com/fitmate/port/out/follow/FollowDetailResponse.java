package com.fitmate.port.out.follow;

public record FollowDetailResponse(
        Long accountId,
        Long profileImageId,
        String nickName
) {
}
