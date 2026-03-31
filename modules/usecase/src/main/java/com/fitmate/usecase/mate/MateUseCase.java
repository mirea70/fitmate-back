package com.fitmate.usecase.mate;

import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.command.MateListCommand;
import com.fitmate.port.in.mate.command.MateModifyCommand;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.file.LoadAttachFilePort;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import com.fitmate.usecase.UseCase;
import com.fitmate.usecase.mate.mapper.MateUseCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@UseCase
@RequiredArgsConstructor
@Transactional
public class MateUseCase implements MateUseCasePort {

    private final LoadMatePort loadMatePort;
    private final LoadAccountPort loadAccountPort;
    private final LoadAttachFilePort loadAttachFilePort;
    private final MateUseCaseMapper mateUseCaseMapper;

    @Override
    public void registerMate(MateCreateCommand mateCreateCommand) {
        Set<Long> introImageIds = mateCreateCommand.getIntroImageIds();
        if(introImageIds != null && !introImageIds.isEmpty())
            loadAttachFilePort.checkExistFiles(introImageIds);
        Mate mate = mateUseCaseMapper.commandToDomain(mateCreateCommand);
        mate.autoApproveWriter();
        Long mateEntityId = loadMatePort.saveMateEntity(mate);
        loadMatePort.saveMateFeeEntities(mate.getMateFees(), new MateId(mateEntityId));
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
    }
}
