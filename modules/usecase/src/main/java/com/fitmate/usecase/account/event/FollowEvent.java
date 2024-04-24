package com.fitmate.usecase.account.event;

import com.fitmate.usecase.account.event.dto.FollowEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowEvent {
    private final FollowEventDto eventDto;
}
