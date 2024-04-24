package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateRequestEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateRequestEvent {
    private final MateRequestEventDto eventDto;
}
