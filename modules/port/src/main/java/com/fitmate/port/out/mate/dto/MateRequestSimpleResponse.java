package com.fitmate.port.out.mate.dto;

import com.fitmate.domain.mate.FitPlace;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.enums.FitCategory;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MateRequestSimpleResponse {
    private final Long mateId;
    private final Long thumbnailImageId;
    private final String title;
    private final LocalDateTime mateAt;
    private final FitPlace fitPlace;
    private final Integer permitPeopleCnt;
    private final Integer approvedAccountCnt;
    private final Integer totalFee;
    private final LocalDateTime applyAt;
    private final boolean closed;
    private final FitCategory fitCategory;
    private final ApproveStatus approveStatus;

    public MateRequestSimpleResponse(Long mateId, Long thumbnailImageId, String title, LocalDateTime mateAt, FitPlace fitPlace, Integer permitPeopleCnt, Integer approvedAccountCnt, Integer totalFee, LocalDateTime applyAt, boolean closed, FitCategory fitCategory, ApproveStatus approveStatus) {
        this.mateId = mateId;
        this.thumbnailImageId = thumbnailImageId;
        this.title = title;
        this.mateAt = mateAt;
        this.fitPlace = fitPlace;
        this.permitPeopleCnt = permitPeopleCnt;
        this.approvedAccountCnt = approvedAccountCnt;
        this.totalFee = totalFee;
        this.applyAt = applyAt;
        this.closed = closed;
        this.fitCategory = fitCategory;
        this.approveStatus = approveStatus;
    }
}
