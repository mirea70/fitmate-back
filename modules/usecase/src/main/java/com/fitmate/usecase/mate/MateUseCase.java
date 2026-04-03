package com.fitmate.usecase.mate;

import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.LimitErrorResult;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateId;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.command.MateListCommand;
import com.fitmate.port.in.mate.command.MateModifyCommand;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.file.LoadAttachFilePort;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import com.fitmate.usecase.UseCase;
import com.fitmate.usecase.mate.event.MateAutoCancelledEvent;
import com.fitmate.usecase.mate.event.MateClosedEvent;
import com.fitmate.usecase.mate.event.MateModifiedEvent;
import com.fitmate.usecase.mate.event.MateRegisteredEvent;
import com.fitmate.usecase.mate.event.dto.MateAutoCancelledEventDto;
import com.fitmate.usecase.mate.event.dto.MateClosedEventDto;
import com.fitmate.usecase.mate.event.dto.MateModifiedEventDto;
import com.fitmate.usecase.mate.event.dto.MateRegisteredEventDto;
import com.fitmate.usecase.mate.mapper.MateUseCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UseCase
@RequiredArgsConstructor
@Transactional
public class MateUseCase implements MateUseCasePort {

    private final LoadMatePort loadMatePort;
    private final LoadMateRequestPort loadMateRequestPort;
    private final LoadAccountPort loadAccountPort;
    private final LoadAttachFilePort loadAttachFilePort;
    private final LoadMateWishPort loadMateWishPort;
    private final MateUseCaseMapper mateUseCaseMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerMate(MateCreateCommand mateCreateCommand) {
        Set<Long> introImageIds = mateCreateCommand.getIntroImageIds();
        if(introImageIds != null && !introImageIds.isEmpty())
            loadAttachFilePort.checkExistFiles(introImageIds);
        Mate mate = mateUseCaseMapper.commandToDomain(mateCreateCommand);
        mate.autoApproveWriter();
        Long mateEntityId = loadMatePort.saveMateEntity(mate);
        loadMatePort.saveMateFeeEntities(mate.getMateFees(), new MateId(mateEntityId));

        eventPublisher.publishEvent(new MateRegisteredEvent(
                new MateRegisteredEventDto(mate.getTitle(), mateEntityId, mateCreateCommand.getWriterId())
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public MateDetailResponse findMate(Long id) {
        Mate mate = loadMatePort.loadMateEntity(new MateId(id));
        Account writer = loadAccountPort.loadAccountEntity(new AccountId(mate.getWriterId()));
        return mateUseCaseMapper.domainToDetailResponse(mate, writer);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<MateSimpleResponse> findAllMate(MateListCommand command) {
        return loadMatePort.loadMates(command);
    }

    @Override
    public void modifyMate(MateModifyCommand command) {
        Loaded<Mate> loadedMate = loadMatePort.loadMate(new MateId(command.getMateId()));
        validateModifyCommand(command, loadedMate.get());
        cancelIneligibleWaitingApplicants(command, loadedMate);

        loadedMate.update(mate -> mate.update(
                command.getFitCategory(),
                command.getTitle(),
                command.getIntroduction(),
                command.getIntroImageIds(),
                command.getMateAt(),
                command.getFitPlaceName(),
                command.getFitPlaceAddress(),
                command.getGatherType(),
                command.getPermitGender(),
                command.getPermitMaxAge(),
                command.getPermitMinAge(),
                command.getPermitPeopleCnt(),
                command.getMateFees(),
                command.getApplyQuestion()
        ));
        loadMatePort.deleteAllMateFeeByMateId(loadedMate.get().getId());
        loadMatePort.saveMateFeeEntities(loadedMate.get().getMateFees(), loadedMate.get().getId());

        eventPublisher.publishEvent(new MateModifiedEvent(
                new MateModifiedEventDto(loadedMate.get().getTitle(), loadedMate.get().getId().getValue())
        ));
    }

    @Override
    public void closeMate(Long mateId, Long writerId) {
        Loaded<Mate> loadedMate = loadMatePort.loadMate(new MateId(mateId));
        Mate mate = loadedMate.get();
        if (!mate.getWriterId().equals(writerId))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WRITER_ID);

        cancelWaitingApplicantsOnClose(mateId, mate);
        notifyWishersOnClose(mateId, mate);
        loadedMate.update(Mate::close);
    }

    private void notifyWishersOnClose(Long mateId, Mate mate) {
        List<Long> wisherIds = loadMateWishPort.getWisherAccountIds(mateId);
        if (wisherIds == null || wisherIds.isEmpty()) return;

        eventPublisher.publishEvent(new MateClosedEvent(
                new MateClosedEventDto(mate.getTitle(), mateId, mate.getWriterId(), wisherIds)
        ));
    }

    private void cancelWaitingApplicantsOnClose(Long mateId, Mate mate) {
        Set<Long> waitingIds = mate.getWaitingAccountIds();
        if (waitingIds == null || waitingIds.isEmpty()) return;

        String cancelReason = "모집이 마감되어 신청이 자동 취소";
        for (Long waitingId : new ArrayList<>(waitingIds)) {
            mate.cancelApply(waitingId);
            Loaded<com.fitmate.domain.mate.apply.MateApply> loadedApply =
                    loadMateRequestPort.loadMateApply(mateId, waitingId);
            loadedApply.update(apply -> apply.cancel(cancelReason, LocalDateTime.now()));

            eventPublisher.publishEvent(new MateAutoCancelledEvent(
                    new MateAutoCancelledEventDto(mate.getTitle(), mateId, mate.getWriterId(), waitingId, cancelReason)
            ));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateSimpleResponse> findMyMates(Long writerId) {
        return loadMatePort.loadMatesByWriterId(writerId);
    }

    private void validateModifyCommand(MateModifyCommand command, Mate mate) {
        if(!mate.getWriterId().equals(command.getWriterId()))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WRITER_ID);
        Set<Long> introImageIds = command.getIntroImageIds();
        if(introImageIds != null && !introImageIds.isEmpty())
            loadAttachFilePort.checkExistFiles(introImageIds);
        validateRecruitRuleNotChanged(command, mate);
    }

    private void validateRecruitRuleNotChanged(MateModifyCommand command, Mate mate) {
        boolean hasApprovedMembers = mate.getApprovedAccountIds() != null && mate.getApprovedAccountIds().size() > 1;

        if (!hasApprovedMembers) return;

        boolean ruleChanged =
                (command.getPermitGender() != null && command.getPermitGender() != mate.getPermitGender())
                || (command.getPermitPeopleCnt() != null && !command.getPermitPeopleCnt().equals(mate.getPermitPeopleCnt()))
                || (command.getGatherType() != null && command.getGatherType() != mate.getGatherType())
                || (command.getPermitMaxAge() != null && !command.getPermitMaxAge().equals(mate.getPermitAges().getMax()))
                || (command.getPermitMinAge() != null && !command.getPermitMinAge().equals(mate.getPermitAges().getMin()));

        if (ruleChanged)
            throw new LimitException(LimitErrorResult.CANNOT_MODIFY_RECRUIT_RULE);
    }

    private void cancelIneligibleWaitingApplicants(MateModifyCommand command, Loaded<Mate> loadedMate) {
        Mate mate = loadedMate.get();

        PermitGender newGender = command.getPermitGender();
        boolean genderChanged = newGender != null && newGender != PermitGender.ALL && newGender != mate.getPermitGender();

        Integer newMinAge = command.getPermitMinAge();
        Integer newMaxAge = command.getPermitMaxAge();
        boolean ageChanged = (newMinAge != null && !newMinAge.equals(mate.getPermitAges().getMin()))
                || (newMaxAge != null && !newMaxAge.equals(mate.getPermitAges().getMax()));

        if (!genderChanged && !ageChanged) return;

        Set<Long> waitingIds = mate.getWaitingAccountIds();
        if (waitingIds == null || waitingIds.isEmpty()) return;

        int effectiveMinAge = newMinAge != null ? newMinAge : mate.getPermitAges().getMin();
        int effectiveMaxAge = newMaxAge != null ? newMaxAge : mate.getPermitAges().getMax();
        PermitGender effectiveGender = newGender != null ? newGender : mate.getPermitGender();

        Map<Long, String> toCancelWithReason = new HashMap<>();
        for (Long waitingId : waitingIds) {
            Account account = loadAccountPort.loadAccountEntity(new AccountId(waitingId));
            List<String> reasons = new ArrayList<>();

            boolean genderMismatch = effectiveGender != PermitGender.ALL
                    && ((effectiveGender == PermitGender.MALE && account.getGender() == Gender.FEMALE)
                    || (effectiveGender == PermitGender.FEMALE && account.getGender() == Gender.MALE));
            if (genderMismatch) reasons.add("허용 성별 변경");

            int age = account.getAge();
            boolean ageMismatch = age > 0 && (age < effectiveMinAge || age > effectiveMaxAge);
            if (ageMismatch) reasons.add("허용 연령대 변경");

            if (!reasons.isEmpty()) {
                toCancelWithReason.put(waitingId, String.join(", ", reasons));
            }
        }

        for (var entry : toCancelWithReason.entrySet()) {
            Long cancelId = entry.getKey();
            String reason = entry.getValue();
            String cancelReason = reason + "으로 인해 신청이 자동 취소";

            loadedMate.update(m -> m.cancelApply(cancelId));
            Loaded<com.fitmate.domain.mate.apply.MateApply> loadedApply =
                    loadMateRequestPort.loadMateApply(command.getMateId(), cancelId);
            loadedApply.update(apply -> apply.cancel(cancelReason, LocalDateTime.now()));

            eventPublisher.publishEvent(new MateAutoCancelledEvent(
                    new MateAutoCancelledEventDto(mate.getTitle(), command.getMateId(), mate.getWriterId(), cancelId, cancelReason)
            ));
        }
    }
}
