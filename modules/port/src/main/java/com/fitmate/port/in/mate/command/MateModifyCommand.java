package com.fitmate.port.in.mate.command;

import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record MateModifyCommand(
        Long mateId,
        FitCategory fitCategory,
        String title,
        String introduction,
        Set<Long> introImageIds,
        LocalDateTime mateAt,
        String fitPlaceName,
        String fitPlaceAddress,
        GatherType gatherType,
        PermitGender permitGender,
        Integer permitMaxAge,
        Integer permitMinAge,
        Integer permitPeopleCnt,
        List<MateFee> mateFees,
        String applyQuestion,
        Long writerId
) {
}
