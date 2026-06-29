package com.fitmate.port.out.mate.dto;

import com.fitmate.domain.mate.FitPlace;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.enums.FitCategory;

import java.time.LocalDateTime;

public record MateRequestSimpleResponse(
        Long mateId,
        Long thumbnailImageId,
        String title,
        LocalDateTime mateAt,
        FitPlace fitPlace,
        Integer permitPeopleCnt,
        Integer approvedAccountCnt,
        Integer totalFee,
        LocalDateTime applyAt,
        boolean closed,
        FitCategory fitCategory,
        ApproveStatus approveStatus
) {
}
