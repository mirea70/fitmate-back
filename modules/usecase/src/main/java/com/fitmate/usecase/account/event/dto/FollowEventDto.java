package com.fitmate.usecase.account.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowEventDto {
    private final Long fromAccountId;
    private final String fromNickName;
    private final Long targetAccountId;
    private final String targetNickName;
}
