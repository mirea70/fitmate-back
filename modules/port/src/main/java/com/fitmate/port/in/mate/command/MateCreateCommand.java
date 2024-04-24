package com.fitmate.port.in.mate.command;

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
public class MateCreateCommand {
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
    private List<MateFee> mateFees;
    private String applyQuestion;
    private Long writerId;
}
