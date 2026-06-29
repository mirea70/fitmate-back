package com.fitmate.usecase.mate.event.dto;

import java.util.List;

public record MateClosedEventDto(
        String title,
        Long mateId,
        Long writerId,
        List<Long> wisherAccountIds
) {
}
