package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class MateRequestSimpleJpaResponse {
    private final Long mateId;
    private final Long thumbnailImageId;
    private final String title;
    private final LocalDateTime mateAt;
    private final String fitPlaceName;
    private final String fitPlaceAddress;
    private final Integer permitPeopleCnt;
    private final Integer approvedAccountCnt;
    private final Integer totalFee;
    private final LocalDateTime applyAt;

    @QueryProjection
    public MateRequestSimpleJpaResponse(Long mateId, Set<Long> introImageIds, String title, LocalDateTime mateAt, String fitPlaceName, String fitPlaceAddress, Integer permitPeopleCnt, Set<Long> approvedAccountIds, Integer totalFee, LocalDateTime applyAt) {
        this.mateId = mateId;
        this.thumbnailImageId = getThumbnailFileId(introImageIds);
        this.title = title;
        this.mateAt = mateAt;
        this.fitPlaceName = fitPlaceName;
        this.fitPlaceAddress = fitPlaceAddress;
        this.permitPeopleCnt = permitPeopleCnt;
        this.approvedAccountCnt = getApprovedAccountCnt(approvedAccountIds);
        this.totalFee = totalFee;
        this.applyAt = applyAt;
    }

    private Long getThumbnailFileId(Set<Long> introImageIds) {
        if(introImageIds == null) return null;
        long min = 0L;
        for (Long introImageId : introImageIds) {
            min = Math.min(min, introImageId);
        }
        return min;
    }

    private Integer getApprovedAccountCnt(Set<Long> approvedAccountIds) {
        if(approvedAccountIds == null || approvedAccountIds.isEmpty())
            return null;
        else
            return approvedAccountIds.size();
    }
}
