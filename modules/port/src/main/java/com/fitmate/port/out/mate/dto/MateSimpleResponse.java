package com.fitmate.port.out.mate.dto;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;

import java.time.LocalDateTime;

public record MateSimpleResponse(
        Long id,
        Long thumbnailImageId,
        Long writerImageId,
        String writerNickName,
        FitCategory fitCategory,
        String title,
        String fitPlaceAddress,
        LocalDateTime mateAt,
        GatherType gatherType,
        PermitGender permitGender,
        Integer permitPeopleCnt,
        int approvedAccountCnt,
        boolean closed
) {
}
