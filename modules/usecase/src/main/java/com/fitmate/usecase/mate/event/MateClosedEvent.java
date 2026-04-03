package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateClosedEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateClosedEvent {
    private final MateClosedEventDto eventDto;
}
