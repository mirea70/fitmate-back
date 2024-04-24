package com.fitmate.usecase.mate;

import com.fitmate.domain.mate.aggregate.Mate;
import com.fitmate.domain.mate.vo.MateId;
import com.fitmate.port.in.mate.command.MateCreateCommand;
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
        loadMatePort.saveMateFeeEntities(mate.getMateFees(), mateEntityId);
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
}
