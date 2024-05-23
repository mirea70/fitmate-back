package com.fitmate.adapter.in.web.mate.mapper;

import com.fitmate.adapter.in.web.mate.dto.MateApplyRequest;
import com.fitmate.adapter.in.web.mate.dto.MateApproveRequest;
import com.fitmate.adapter.in.web.mate.dto.MateCreateRequest;
import com.fitmate.adapter.in.web.mate.dto.MateModifyRequest;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.command.MateModifyCommand;
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
}
