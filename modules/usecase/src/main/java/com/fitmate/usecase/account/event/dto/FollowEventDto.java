package com.fitmate.usecase.account.event.dto;

import lombok.Getter;

@Getter
public class FollowEventDto {
    private final Long fromAccountId;
    private final Long targetAccountId;
    private final String targetNickName;

    public FollowEventDto(Long fromAccountId, Long targetAccountId, String targetNickName) {
        this.fromAccountId = fromAccountId;
        this.targetAccountId = targetAccountId;
        this.targetNickName = targetNickName;
    }
}
