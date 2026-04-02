package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateAutoCancelledEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateAutoCancelledEvent {
    private final MateAutoCancelledEventDto eventDto;
}
