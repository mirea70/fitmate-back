package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class MateSimpleJpaResponse {
    private final Long id;
    private final String fitCategory;
    private final String title;
    private final LocalDateTime mateAt;
    private final String fitPlaceName;
    private final String fitPlaceAddress;
    private final String gatherType;
    private final String permitGender;
    private final Integer permitMaxAge;
    private final Integer permitMinAge;
    private final Integer permitPeopleCnt;
    private final Set<Long> waitingAccountIds;
    private final Set<Long> approvedAccountIds;
    private final Set<Long> introImageIds;

    @QueryProjection
    public MateSimpleJpaResponse(Long id, String fitCategory, String title, LocalDateTime mateAt, String fitPlaceName, String fitPlaceAddress, String gatherType, String permitGender, Integer permitMaxAge, Integer permitMinAge, Integer permitPeopleCnt, Set<Long> waitingAccountIds, Set<Long> approvedAccountIds, Set<Long> introImageIds) {
        this.id = id;
        this.fitCategory = fitCategory;
        this.title = title;
        this.mateAt = mateAt;
        this.fitPlaceName = fitPlaceName;
        this.fitPlaceAddress = fitPlaceAddress;
        this.gatherType = gatherType;
        this.permitGender = permitGender;
        this.permitMaxAge = permitMaxAge;
        this.permitMinAge = permitMinAge;
        this.permitPeopleCnt = permitPeopleCnt;
        this.waitingAccountIds = waitingAccountIds;
        this.approvedAccountIds = approvedAccountIds;
        this.introImageIds = introImageIds;
    }
}
