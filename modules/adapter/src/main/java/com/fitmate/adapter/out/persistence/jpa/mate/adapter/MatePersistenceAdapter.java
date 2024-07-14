package com.fitmate.adapter.out.persistence.jpa.mate.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateFeeJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.mapper.MatePersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateFeeRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRepository;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.common.SliceCommand;
import com.fitmate.port.in.mate.command.MateListCommand;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MatePersistenceAdapter implements LoadMatePort {

    private final MatePersistenceMapper matePersistenceMapper;
    private final MateRepository mateRepository;
    private final MateFeeRepository mateFeeRepository;
    private final MateQueryRepository mateQueryRepository;

    @Override
    public Long saveMateEntity(Mate mate) {
        MateJpaEntity mateEntity = matePersistenceMapper.domainToEntity(mate);
        return mateRepository.save(mateEntity).getId();
    }

    @Override
    public void saveMateFeeEntities(List<MateFee> mateFees, MateId mateId) {
        List<MateFeeJpaEntity> mateFeeEntities = matePersistenceMapper.domainsToEntities(mateFees, mateId.getValue());
        mateFeeRepository.saveAll(mateFeeEntities);
    }

    @Override
    public Mate loadMateEntity(MateId id) {
        MateJpaEntity mateEntity = mateRepository.getById(id.getValue());
        List<MateFeeJpaEntity> mateFeeEntities = mateFeeRepository.findAllByMateId(id.getValue());
        return matePersistenceMapper.entityToDomain(mateEntity, mateFeeEntities);
    }

    @Override
    public SliceResponse<MateSimpleResponse> loadMates(MateListCommand command) {
        List<MateSimpleJpaResponse> jpaResponses = mateQueryRepository.readList(command);
        return matePersistenceMapper.jpaResponsesToSliceResponse(jpaResponses, command);
    }

    @Override
    public void deleteAllMateByWriter(AccountId id) {
        mateRepository.deleteAllByWriterId(id.getValue());
    }

    @Override
    public void deleteAllMateFeeByMateId(MateId id) {
        mateFeeRepository.deleteAllByMateId(id.getValue());
    }
}
