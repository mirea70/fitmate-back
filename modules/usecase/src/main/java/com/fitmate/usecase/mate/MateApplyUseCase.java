package com.fitmate.usecase.mate;

import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import com.fitmate.usecase.UseCase;
import com.fitmate.usecase.mate.event.MateApproveEvent;
import com.fitmate.usecase.mate.event.dto.MateApproveEventDto;
import com.fitmate.usecase.mate.event.MateRequestEvent;
import com.fitmate.usecase.mate.event.dto.MateRequestEventDto;
import com.fitmate.usecase.mate.mapper.MateUseCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional
public class MateApplyUseCase implements MateApplyUseCasePort {

    private final LoadMatePort loadMatePort;
    private final LoadMateRequestPort loadMateRequestPort;
    private final MateUseCaseMapper mateUseCaseMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public MateQuestionResponse readQuestion(Long mateId) {
        return loadMateRequestPort.loadMateQuestion(mateId);
    }

    @Override
    public void applyMate(MateApplyCommand mateApplyCommand) {
        Mate mate = loadMatePort.loadMateEntity(new MateId(mateApplyCommand.getMateId()));
        Long applierId = mateApplyCommand.getApplierId();
        if(applierId.equals(mate.getWriterId()))
            throw new NotMatchException(NotMatchErrorResult.CANNOT_APPLY_WRITER);

        ApproveStatus approveStatus = saveNewMateRequest(mateApplyCommand, mate.getGatherType());
        if(approveStatus == ApproveStatus.APPROVE)
            mate.addApprovedAccountId(applierId);
        else
            mate.addWaitingAccountId(applierId);
        loadMatePort.saveMateEntity(mate);

        publishMateRequestEvent(mate, applierId);
    }

    private ApproveStatus saveNewMateRequest(MateApplyCommand mateApplyCommand, GatherType gatherType) {
        loadMateRequestPort.isDuplicateMateRequest(mateApplyCommand.getMateId(), mateApplyCommand.getApplierId());
        ApproveStatus approveStatus;
        if(gatherType == GatherType.FAST)
            approveStatus = ApproveStatus.APPROVE;
        else
            approveStatus = ApproveStatus.WAIT;
        MateApply mateApply = mateUseCaseMapper.commandToDomain(mateApplyCommand, approveStatus);
        loadMateRequestPort.saveMateRequestEntity(mateApply);

        return approveStatus;
    }

    private void publishMateRequestEvent(Mate mate, Long applierId) {
        MateRequestEventDto eventDto = new MateRequestEventDto(
                mate.getTitle(),
                mate.getId().getValue(),
                applierId
        );
        MateRequestEvent event = new MateRequestEvent(eventDto);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void approveMate(MateApproveCommand mateApproveCommand) {
        Long mateId = mateApproveCommand.getMateId();
        Long applierId = mateApproveCommand.getApplierId();

        Mate mate = loadMatePort.loadMateEntity(new MateId(mateApproveCommand.getMateId()));
        if(!mateApproveCommand.getAccountId().equals(mate.getWriterId()))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WRITER_ID);

        doApproveMateRequest(mateId, applierId);
        mate.approve(applierId);
        loadMatePort.saveMateEntity(mate);
        publishMateApproveEvent(mate, applierId);
    }

    private void doApproveMateRequest(Long mateId, Long applierId) {
        MateApply mateApply = loadMateRequestPort.loadMateRequestEntity(mateId, applierId);
        mateApply.changeToApprove();
        loadMateRequestPort.saveMateRequestEntity(mateApply);
    }

    private void publishMateApproveEvent(Mate mate, Long applierId) {
        MateApproveEventDto eventDto = new MateApproveEventDto(
                mate.getTitle(),
                mate.getId().getValue(),
                applierId
        );
        MateApproveEvent event = new MateApproveEvent(eventDto);
        eventPublisher.publishEvent(event);
    }
}
