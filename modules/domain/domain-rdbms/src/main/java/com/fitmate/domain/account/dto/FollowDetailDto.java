package com.fitmate.domain.account.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class FollowDetailDto {
    private Long accountId;
    private Long profileImageId;
    private String nickName;

    @QueryProjection
    public FollowDetailDto(Long accountId, Long profileImageId, String nickName) {
        this.accountId = accountId;
        this.profileImageId = profileImageId;
        this.nickName = nickName;
    }
}
