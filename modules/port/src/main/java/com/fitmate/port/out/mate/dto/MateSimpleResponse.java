package com.fitmate.port.out.mate.dto;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class MateSimpleResponse {
    private Long id;
    private Long thumbnailImageId;
    private Long writerImageId;
    private String writerNickName;
    private FitCategory fitCategory;
    private String title;
    private String fitPlaceAddress;
    private LocalDateTime mateAt;
    private GatherType gatherType;
    private PermitGender permitGender;
    private Integer permitPeopleCnt;
    private int approvedAccountCnt;
}
