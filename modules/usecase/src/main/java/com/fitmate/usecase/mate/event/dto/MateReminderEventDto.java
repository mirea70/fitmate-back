package com.fitmate.usecase.mate.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateReminderEventDto {
    private Long mateId;
    private Long accountId;
    private String title;
}
