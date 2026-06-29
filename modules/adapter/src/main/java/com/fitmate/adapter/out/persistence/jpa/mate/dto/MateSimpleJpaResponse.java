package com.fitmate.adapter.out.persistence.jpa.mate.dto;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.Set;

public record MateSimpleJpaResponse(
        Long id,
        Long thumbnailImageId,
        Long writerImageId,
        String writerNickName,
        String fitCategory,
        String title,
        String fitPlaceAddress,
        LocalDateTime mateAt,
        String gatherType,
        String permitGender,
        Integer permitPeopleCnt,
        int approvedAccountCnt,
        boolean closed
) {
    @QueryProjection
    public MateSimpleJpaResponse(Long id, Set<Long> introImageIds, Long writerImageId, String writerNickName,
                                 String fitCategory, String title, String fitPlaceAddress, LocalDateTime mateAt,
                                 String gatherType, String permitGender, Integer permitPeopleCnt, int approvedCount,
                                 LocalDateTime closedAt) {
        this(id, getThumbnailFileId(introImageIds), writerImageId, writerNickName, fitCategory, title,
                fitPlaceAddress, mateAt, gatherType, permitGender, permitPeopleCnt, approvedCount, closedAt != null);
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
