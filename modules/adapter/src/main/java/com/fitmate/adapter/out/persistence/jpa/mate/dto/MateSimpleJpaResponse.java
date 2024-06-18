package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class MateSimpleJpaResponse {
    private final Long id;
    private final Long thumbnailImageId;
    private final Long writerImageId;
    private final String writerNickName;
    private final String fitCategory;
    private final String title;
    private final String fitPlaceAddress;
    private final LocalDateTime mateAt;
    private final String gatherType;
    private final String permitGender;
    private final Integer permitPeopleCnt;
    private final int approvedAccountCnt;

    @QueryProjection
    public MateSimpleJpaResponse(Long id, Set<Long> introImageIds, Long writerImageId, String writerNickName, String fitCategory, String title, String fitPlaceAddress, LocalDateTime mateAt, String gatherType, String permitGender, Integer permitPeopleCnt, Set<Long> approvedAccountIds) {
        this.id = id;
        this.thumbnailImageId = getThumbnailId(introImageIds);
        this.writerImageId = writerImageId;
        this.writerNickName = writerNickName;
        this.fitCategory = fitCategory;
        this.title = title;
        this.fitPlaceAddress = fitPlaceAddress;
        this.mateAt = mateAt;
        this.gatherType = gatherType;
        this.permitGender = permitGender;
        this.permitPeopleCnt = permitPeopleCnt;
        this.approvedAccountCnt = approvedAccountIds.size();
    }

    private Long getThumbnailId(Set<Long> introImageIds) {
        if(introImageIds == null || introImageIds.isEmpty()) return null;
        for(Long imageId : introImageIds) {
            return imageId;
        }
        return null;
    }
}
