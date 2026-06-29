package com.fitmate.port.in.mate.command;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.port.in.common.SliceCommand;

import java.time.LocalDateTime;
import java.util.List;

public record MateListCommand(
        Integer page,
        Integer size,
        SliceCommand.SortDir sortDir,
        String sortProperty,
        String keyword,
        Integer dayOfWeek,
        LocalDateTime startMateAt,
        LocalDateTime endMateAt,
        List<String> fitPlaceRegions,
        Integer permitMaxAge,
        Integer permitMinAge,
        Integer startLimitPeopleCnt,
        Integer endLimitPeopleCnt,
        FitCategory fitCategory,
        Boolean includeClosed
) implements SliceCommand {
}
