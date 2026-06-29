package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.Set;

public record MateApplySimpleJpaResponse(
        Long mateId,
        Long thumbnailImageId,
        String title,
        LocalDateTime mateAt,
        String fitPlaceName,
        String fitPlaceAddress,
        Integer permitPeopleCnt,
        Integer approvedAccountCnt,
        Integer totalFee,
        LocalDateTime applyAt,
        boolean closed,
        String fitCategory,
        String approveStatus
) {
    @QueryProjection
    public MateApplySimpleJpaResponse(Long mateId, Set<Long> introImageIds, String title, LocalDateTime mateAt,
                                      String fitPlaceName, String fitPlaceAddress, Integer permitPeopleCnt,
                                      int approvedCount, Integer totalFee, LocalDateTime applyAt,
                                      LocalDateTime closedAt, String fitCategory, String approveStatus) {
        this(mateId, getThumbnailFileId(introImageIds), title, mateAt, fitPlaceName, fitPlaceAddress,
                permitPeopleCnt, approvedCount, totalFee, applyAt, closedAt != null, fitCategory, approveStatus);
    }

    private static Long getThumbnailFileId(Set<Long> introImageIds) {
        if (introImageIds == null) return null;
        long min = Long.MAX_VALUE;
        for (Long introImageId : introImageIds) {
            min = Math.min(min, introImageId);
        }
        return min;
    }
}
