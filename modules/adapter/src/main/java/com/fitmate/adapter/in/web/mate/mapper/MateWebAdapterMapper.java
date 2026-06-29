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
                request.getFitCategory(),
                request.getTitle(),
                request.getIntroduction(),
                introImageIds,
                request.getMateAt(),
                request.getFitPlaceName(),
                request.getFitPlaceAddress(),
                request.getGatherType(),
                request.getPermitGender(),
                request.getPermitMaxAge(),
                request.getPermitMinAge(),
                request.getPermitPeopleCnt(),
                request.getMateFees(),
                request.getApplyQuestion(),
                writerId
        );
    }

    public MateModifyCommand requestToCommand(Long mateId, MateModifyRequest request, Long writerId) {
        return new MateModifyCommand(
                mateId,
                request.getFitCategory(),
                request.getTitle(),
                request.getIntroduction(),
                request.getIntroImageIds(),
                request.getMateAt(),
                request.getFitPlaceName(),
                request.getFitPlaceAddress(),
                request.getGatherType(),
                request.getPermitGender(),
                request.getPermitMaxAge(),
                request.getPermitMinAge(),
                request.getPermitPeopleCnt(),
                request.getMateFees(),
                request.getApplyQuestion(),
                writerId
        );
    }

    public MateApplyCommand requestToCommand(MateApplyRequest request, Long mateId, Long accountId) {
        return new MateApplyCommand(
                mateId,
                accountId,
                request.getComeAnswer()
        );
    }

    public MateApproveCommand requestToCommand(MateApproveRequest request, Long mateId, Long accountId) {
        return new MateApproveCommand(
                mateId,
                request.getApplierId(),
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
