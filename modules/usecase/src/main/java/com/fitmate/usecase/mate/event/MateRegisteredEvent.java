package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateRegisteredEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateRegisteredEvent {
    private final MateRegisteredEventDto eventDto;
}
