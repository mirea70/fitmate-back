package com.fitmate.adapter.out.persistence.jpa.mate.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApprovedCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateFeeJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.mapper.MatePersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateApprovedCountRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateFeeRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRepository;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.in.mate.command.MateListCommand;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MatePersistenceAdapter implements LoadMatePort {

    private final MatePersistenceMapper matePersistenceMapper;
    private final MateRepository mateRepository;
    private final MateFeeRepository mateFeeRepository;
    private final MateQueryRepository mateQueryRepository;
    private final MateApprovedCountRepository mateApprovedCountRepository;

    @Override
    public Long saveMateEntity(Mate mate) {
        MateJpaEntity mateEntity = matePersistenceMapper.domainToEntity(mate);
        Long mateId = mateRepository.save(mateEntity).getId();
        mateApprovedCountRepository.save(new MateApprovedCountJpaEntity(mateId));
        return mateId;
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
        int approvedCount = getApprovedCount(id.getValue());
        return matePersistenceMapper.entityToDomain(mateEntity, mateFeeEntities, approvedCount);
    }

    @Override
    public Loaded<Mate> loadMate(MateId id) {
        MateJpaEntity entity = mateRepository.getById(id.getValue());
        List<MateFeeJpaEntity> mateFeeEntities = mateFeeRepository.findAllByMateId(id.getValue());
        int approvedCount = getApprovedCount(id.getValue());
        Mate domain = matePersistenceMapper.entityToDomain(entity, mateFeeEntities, approvedCount);
        return new Loaded<>(domain, updated -> matePersistenceMapper.syncToEntity(entity, updated));
    }

    @Override
    public SliceResponse<MateSimpleResponse> loadMates(MateListCommand command) {
        List<MateSimpleJpaResponse> jpaResponses = mateQueryRepository.readList(command);
        return matePersistenceMapper.jpaResponsesToSliceResponse(jpaResponses, command);
    }

    @Override
    public List<MateSimpleResponse> loadMatesByIds(List<Long> mateIds) {
        if (mateIds == null || mateIds.isEmpty()) return List.of();
        List<MateSimpleJpaResponse> jpaResponses = mateQueryRepository.readListByIds(mateIds);
        return matePersistenceMapper.jpaResponsesToResponses(jpaResponses);
    }

    @Override
    public List<MateSimpleResponse> loadMatesByWriterId(Long writerId) {
        List<MateSimpleJpaResponse> jpaResponses = mateQueryRepository.readListByWriterId(writerId);
        return matePersistenceMapper.jpaResponsesToResponses(jpaResponses);
    }

    @Override
    public void incrementApprovedCount(MateId id) {
        MateApprovedCountJpaEntity countEntity = mateApprovedCountRepository.getById(id.getValue());
        countEntity.increment();
    }

    @Override
    public void decrementApprovedCount(MateId id) {
        MateApprovedCountJpaEntity countEntity = mateApprovedCountRepository.getById(id.getValue());
        countEntity.decrement();
    }

    @Override
    public List<Mate> loadMatesByMateAtBetween(LocalDateTime from, LocalDateTime to) {
        return mateRepository.findAllByMateAtBetween(from, to).stream()
                .map(entity -> {
                    List<MateFeeJpaEntity> fees = mateFeeRepository.findAllByMateId(entity.getId());
                    int approvedCount = getApprovedCount(entity.getId());
                    return matePersistenceMapper.entityToDomain(entity, fees, approvedCount);
                })
                .toList();
    }

    @Override
    public List<Loaded<Mate>> loadUnclosedMatesBeforeMateAt(LocalDateTime before) {
        return mateRepository.findAllByMateAtBeforeAndClosedAtIsNull(before).stream()
                .map(entity -> {
                    List<MateFeeJpaEntity> fees = mateFeeRepository.findAllByMateId(entity.getId());
                    int approvedCount = getApprovedCount(entity.getId());
                    Mate domain = matePersistenceMapper.entityToDomain(entity, fees, approvedCount);
                    return new Loaded<>(domain, updated -> matePersistenceMapper.syncToEntity(entity, updated));
                })
                .toList();
    }

    @Override
    public void deleteAllMateByWriter(AccountId id) {
        mateRepository.deleteAllByWriterId(id.getValue());
    }

    @Override
    public void deleteAllMateFeeByMateId(MateId id) {
        mateFeeRepository.deleteAllByMateId(id.getValue());
    }

    private int getApprovedCount(Long mateId) {
        return mateApprovedCountRepository.findById(mateId)
                .map(MateApprovedCountJpaEntity::getApprovedCount)
                .orElse(0);
    }
}
