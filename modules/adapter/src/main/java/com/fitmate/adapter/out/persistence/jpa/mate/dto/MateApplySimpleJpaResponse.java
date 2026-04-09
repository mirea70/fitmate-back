package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class MateApplySimpleJpaResponse {
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
    private final boolean closed;
    private final String fitCategory;
    private final String approveStatus;

    @QueryProjection
    public MateApplySimpleJpaResponse(Long mateId, Set<Long> introImageIds, String title, LocalDateTime mateAt, String fitPlaceName, String fitPlaceAddress, Integer permitPeopleCnt, int approvedCount, Integer totalFee, LocalDateTime applyAt, LocalDateTime closedAt, String fitCategory, String approveStatus) {
        this.mateId = mateId;
        this.thumbnailImageId = getThumbnailFileId(introImageIds);
        this.title = title;
        this.mateAt = mateAt;
        this.fitPlaceName = fitPlaceName;
        this.fitPlaceAddress = fitPlaceAddress;
        this.permitPeopleCnt = permitPeopleCnt;
        this.approvedAccountCnt = approvedCount;
        this.totalFee = totalFee;
        this.applyAt = applyAt;
        this.closed = closedAt != null;
        this.fitCategory = fitCategory;
        this.approveStatus = approveStatus;
    }

    private Long getThumbnailFileId(Set<Long> introImageIds) {
        if(introImageIds == null) return null;
        long min = Long.MAX_VALUE;
        for (Long introImageId : introImageIds) {
            min = Math.min(min, introImageId);
        }
        return min;
    }
}
