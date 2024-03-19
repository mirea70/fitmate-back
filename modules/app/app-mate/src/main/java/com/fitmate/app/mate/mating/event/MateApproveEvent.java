package com.fitmate.app.mate.mating.event;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateApproveEvent {
    private final MateEventDto.Approve eventDto;
}
