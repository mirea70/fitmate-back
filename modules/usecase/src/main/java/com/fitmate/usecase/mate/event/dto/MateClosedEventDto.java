package com.fitmate.usecase.mate.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MateClosedEventDto {
    private final String title;
    private final Long mateId;
    private final Long writerId;
    private final List<Long> wisherAccountIds;
}
