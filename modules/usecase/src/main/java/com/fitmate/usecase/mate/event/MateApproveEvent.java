package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateApproveEventDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MateApproveEvent {
    private final MateApproveEventDto eventDto;
}
