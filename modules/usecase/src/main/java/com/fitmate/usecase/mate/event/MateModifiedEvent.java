package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateModifiedEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateModifiedEvent {
    private final MateModifiedEventDto eventDto;
}
