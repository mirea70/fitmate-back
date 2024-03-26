package com.fitmate.app.mate.account.event;

import com.fitmate.app.mate.account.dto.AccountEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountDeleteEvent {
    private final AccountEventDto.Delete eventDto;
}
