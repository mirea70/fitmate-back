package com.fitmate.usecase.mate.event;

import com.fitmate.usecase.mate.event.dto.MateReminderEventDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateReminderEvent {
    private final MateReminderEventDto eventDto;
}
