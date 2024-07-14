package com.fitmate.port.in.mate.command;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.port.in.common.SliceCommand;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MateListCommand extends SliceCommand {
    private final String keyword;
    private final Integer dayOfWeek;
    private final LocalDateTime startMateAt;
    private final LocalDateTime endMateAt;
    private final List<String> fitPlaceRegions;
    private final Integer permitMaxAge;
    private final Integer permitMinAge;
    private final Integer startLimitPeopleCnt;
    private final Integer endLimitPeopleCnt;
    private final FitCategory fitCategory;

    public MateListCommand(Integer page, Integer size, SortDir sortDir, String sortProperty, String keyword, Integer dayOfWeek, LocalDateTime startMateAt, LocalDateTime endMateAt, List<String> fitPlaceRegions, Integer permitMaxAge, Integer permitMinAge, Integer startLimitPeopleCnt, Integer endLimitPeopleCnt, FitCategory fitCategory) {
        super(page, size, sortDir, sortProperty);
        this.keyword = keyword;
        this.dayOfWeek = dayOfWeek;
        this.startMateAt = startMateAt;
        this.endMateAt = endMateAt;
        this.fitPlaceRegions = fitPlaceRegions;
        this.permitMaxAge = permitMaxAge;
        this.permitMinAge = permitMinAge;
        this.startLimitPeopleCnt = startLimitPeopleCnt;
        this.endLimitPeopleCnt = endLimitPeopleCnt;
        this.fitCategory = fitCategory;
    }
}
