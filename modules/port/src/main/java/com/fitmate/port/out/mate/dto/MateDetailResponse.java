package com.fitmate.port.out.mate.dto;

import com.fitmate.domain.mate.vo.FitCategory;
import com.fitmate.domain.mate.vo.GatherType;
import com.fitmate.domain.mate.vo.MateFee;
import com.fitmate.domain.mate.vo.PermitGender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class MateDetailResponse {
    private Long id;
    private FitCategory fitCategory;
    private String title;
    private String introduction;
    private Set<Long> introImageIds;
    private LocalDateTime mateAt;
    private String fitPlaceName;
    private String fitPlaceAddress;
    private GatherType gatherType;
    private PermitGender permitGender;
    private Integer permitMaxAge;
    private Integer permitMinAge;
    private Integer permitPeopleCnt;
    private Integer totalFee;
    private List<MateFee> mateFees;
    private String applyQuestion;
    private Set<Long> waitingAccountIds;
    private Set<Long> approvedAccountIds;
}
