package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateCancelledEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateCancelledEvent {
    private final MateCancelledEventDto eventDto;
}
