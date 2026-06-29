package com.fitmate.usecase.mate.mapper;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.ProfileInfo;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.FitPlace;
import com.fitmate.domain.mate.PermitAges;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MateUseCaseMapper {
    public Mate commandToDomain(MateCreateCommand createCommand) {

        FitPlace fitPlace = new FitPlace(
                createCommand.fitPlaceName(),
                createCommand.fitPlaceAddress()
        );

        PermitAges permitAges = new PermitAges(
                createCommand.permitMaxAge(),
                createCommand.permitMinAge()
        );

        return Mate.withoutId(
                createCommand.fitCategory(),
                createCommand.title(),
                createCommand.introduction(),
                createCommand.introImageIds(),
                createCommand.mateAt(),
                fitPlace,
                createCommand.gatherType(),
                createCommand.permitGender(),
                permitAges,
                createCommand.permitPeopleCnt(),
                createCommand.writerId(),
                createCommand.mateFees(),
                createCommand.applyQuestion()
        );
    }

    public MateDetailResponse domainToDetailResponse(Mate mate, Account writer, Set<Long> waitingAccountIds, Set<Long> approvedAccountIds) {

        FitPlace fitPlace = mate.getFitPlace();
        PermitAges permitAges = mate.getPermitAges();
        ProfileInfo profileInfo = writer.getProfileInfo();

        return new MateDetailResponse(
                mate.getId().getValue(),
                writer.getId().getValue(),
                profileInfo.getNickName(),
                profileInfo.getProfileImageId(),
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
                waitingAccountIds,
                approvedAccountIds,
                mate.isClosed()
        );
    }

    public MateApply commandToDomain(MateApplyCommand command, ApproveStatus approveStatus) {
        return MateApply.withoutId(
                command.comeAnswer(),
                command.mateId(),
                command.applierId(),
                approveStatus,
                null
        );
    }
}
