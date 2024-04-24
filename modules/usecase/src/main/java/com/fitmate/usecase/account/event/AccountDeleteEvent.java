package com.fitmate.usecase.account.event;

import com.fitmate.usecase.account.event.dto.AccountDeleteEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDeleteEvent {
    private final AccountDeleteEventDto eventDto;
}
