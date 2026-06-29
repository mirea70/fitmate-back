package com.fitmate.adapter.in.web.mate.mapper;

import com.fitmate.adapter.in.web.mate.dto.*;
import com.fitmate.port.in.common.SliceCommand;
import com.fitmate.port.in.mate.command.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MateWebAdapterMapper {
    public MateCreateCommand requestToCommand(MateCreateRequest request, Long writerId, Set<Long> introImageIds) {
        return new MateCreateCommand(
                request.fitCategory(),
                request.title(),
                request.introduction(),
                introImageIds,
                request.mateAt(),
                request.fitPlaceName(),
                request.fitPlaceAddress(),
                request.gatherType(),
                request.permitGender(),
                request.permitMaxAge(),
                request.permitMinAge(),
                request.permitPeopleCnt(),
                request.mateFees(),
                request.applyQuestion(),
                writerId
        );
    }

    public MateModifyCommand requestToCommand(Long mateId, MateModifyRequest request, Long writerId) {
        return new MateModifyCommand(
                mateId,
                request.fitCategory(),
                request.title(),
                request.introduction(),
                request.introImageIds(),
                request.mateAt(),
                request.fitPlaceName(),
                request.fitPlaceAddress(),
                request.gatherType(),
                request.permitGender(),
                request.permitMaxAge(),
                request.permitMinAge(),
                request.permitPeopleCnt(),
                request.mateFees(),
                request.applyQuestion(),
                writerId
        );
    }

    public MateApplyCommand requestToCommand(MateApplyRequest request, Long mateId, Long accountId) {
        return new MateApplyCommand(
                mateId,
                accountId,
                request.comeAnswer()
        );
    }

    public MateApproveCommand requestToCommand(MateApproveRequest request, Long mateId, Long accountId) {
        return new MateApproveCommand(
                mateId,
                request.applierId(),
                accountId
        );
    }

    public MateListCommand requestToCommand(MateListRequest request) {
        return new MateListCommand(
                request.page(),
                request.size(),
                SliceCommand.SortDir.valueOf(request.sortDir().name()),
                request.sortProperty(),
                request.keyword(),
                request.dayOfWeek(),
                request.startMateAt(),
                request.endMateAt(),
                request.fitPlaceRegions(),
                request.permitMaxAge(),
                request.permitMinAge(),
                request.startLimitPeopleCnt(),
                request.endLimitPeopleCnt(),
                request.fitCategory(),
                request.includeClosed()
        );
    }
}
