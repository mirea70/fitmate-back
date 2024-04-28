package com.fitmate.usecase.mate;

import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.command.MateModifyCommand;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
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
    private final LoadAttachFilePort loadAttachFilePort;
    private final MateUseCaseMapper mateUseCaseMapper;

    @Override
    public void registerMate(MateCreateCommand mateCreateCommand) {
        Set<Long> introImageIds = mateCreateCommand.getIntroImageIds();
        if(introImageIds != null && !introImageIds.isEmpty())
            loadAttachFilePort.checkExistFiles(introImageIds);
        Mate mate = mateUseCaseMapper.commandToDomain(mateCreateCommand);
        Long mateEntityId = loadMatePort.saveMateEntity(mate);
        loadMatePort.saveMateFeeEntities(mate.getMateFees(), new MateId(mateEntityId));
    }

    @Override
    @Transactional(readOnly = true)
    public MateDetailResponse findMate(Long id) {
        Mate mate = loadMatePort.loadMateEntity(new MateId(id));
        return mateUseCaseMapper.domainToDetailResponse(mate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateSimpleResponse> findAllMate(Long lastMatingId, Integer limit) {
        return loadMatePort.loadMates(lastMatingId, limit);
    }

    @Override
    public void modifyMate(MateModifyCommand command) {
        Mate mate = loadMatePort.loadMateEntity(new MateId(command.getMateId()));
        validateModifyCommand(command, mate);

        mate.update(
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
        );
        loadMatePort.saveMateEntity(mate);
        loadMatePort.deleteAllMateFeeByMateId(mate.getId());
        loadMatePort.saveMateFeeEntities(mate.getMateFees(), mate.getId());
    }

    private void validateModifyCommand(MateModifyCommand command, Mate mate) {
        if(!mate.getWriterId().equals(command.getWriterId()))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WRITER_ID);
        Set<Long> introImageIds = command.getIntroImageIds();
        if(introImageIds != null && !introImageIds.isEmpty())
            loadAttachFilePort.checkExistFiles(introImageIds);
    }
}
