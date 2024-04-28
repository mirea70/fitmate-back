package com.fitmate.port.in.mate.command;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.enums.PermitGender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class MateModifyCommand {
    private Long mateId;
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
