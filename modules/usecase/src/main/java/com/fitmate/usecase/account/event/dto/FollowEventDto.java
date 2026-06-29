package com.fitmate.usecase.account.event.dto;

public record FollowEventDto(
        Long fromAccountId,
        String fromNickName,
        Long targetAccountId,
        String targetNickName
) {
}
