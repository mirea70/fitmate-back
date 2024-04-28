package com.fitmate.usecase.mate.mapper;

import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.FitPlace;
import com.fitmate.domain.mate.PermitAges;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import org.springframework.stereotype.Component;

@Component
public class MateUseCaseMapper {
    public Mate commandToDomain(MateCreateCommand createCommand) {

        FitPlace fitPlace = new FitPlace(
                createCommand.getFitPlaceName(),
                createCommand.getFitPlaceAddress()
        );

        PermitAges permitAges = new PermitAges(
                createCommand.getPermitMaxAge(),
                createCommand.getPermitMinAge()
        );

        return Mate.withoutId(
                createCommand.getFitCategory(),
                createCommand.getTitle(),
                createCommand.getIntroduction(),
                createCommand.getIntroImageIds(),
                createCommand.getMateAt(),
                fitPlace,
                createCommand.getGatherType(),
                createCommand.getPermitGender(),
                permitAges,
                createCommand.getPermitPeopleCnt(),
                createCommand.getWriterId(),
                createCommand.getMateFees(),
                createCommand.getApplyQuestion(),
                null,
                null
        );
    }

    public MateDetailResponse domainToDetailResponse(Mate mate) {

        FitPlace fitPlace = mate.getFitPlace();
        PermitAges permitAges = mate.getPermitAges();

        return new MateDetailResponse(
                mate.getId().getValue(),
                mate.getFitCategory(),
                mate.getTitle(),
                mate.getIntroduction(),
                mate.getIntroImageIds(),
                mate.getMateAt(),
                fitPlace.getName(),
                fitPlace.getAddress(),
                mate.getGatherType(),
                mate.getPermitGender(),
                permitAges.getMax(),
                permitAges.getMin(),
                mate.getPermitPeopleCnt(),
                mate.getTotalFee(),
                mate.getMateFees(),
                mate.getApplyQuestion(),
                mate.getWaitingAccountIds(),
                mate.getApprovedAccountIds()
        );
    }

    public MateApply commandToDomain(MateApplyCommand command, ApproveStatus approveStatus) {
        return MateApply.withoutId(
                command.getComeAnswer(),
                command.getMateId(),
                command.getApplierId(),
                approveStatus
        );
    }
}
