package com.fitmate.port.out.mate.dto;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MateSimpleResponse {
    private Long id;
    private FitCategory fitCategory;
    private String title;
    private LocalDateTime mateAt;
    private String fitPlaceName;
    private String fitPlaceAddress;
    private GatherType gatherType;
    private PermitGender permitGender;
    private Integer permitMaxAge;
    private Integer permitMinAge;
    private Integer permitPeopleCnt;
    private int waitingAccountCnt;
    private int approvedAccountCnt;
}
