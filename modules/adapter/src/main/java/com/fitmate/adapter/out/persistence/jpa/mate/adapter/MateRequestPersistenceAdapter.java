package com.fitmate.adapter.out.persistence.jpa.mate.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateQuestionJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateApplySimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApplyJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.mapper.MatePersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateApplyQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateApplyRepository;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.domain.error.results.DuplicatedErrorResult;
import com.fitmate.domain.mate.apply.MateApply;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import com.fitmate.port.out.mate.dto.MateRequestSimpleResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class MateRequestPersistenceAdapter implements LoadMateRequestPort {

    private final MatePersistenceMapper matePersistenceMapper;
    private final MateQueryRepository mateQueryRepository;
    private final MateApplyRepository mateApplyRepository;
    private final MateApplyQueryRepository mateApplyQueryRepository;

    @Override
    public MateQuestionResponse loadMateQuestion(Long mateId) {
        MateQuestionJpaResponse jpaResponse = mateQueryRepository.readQuestion(mateId);
        return matePersistenceMapper.jpaResponseToResponse(jpaResponse);
    }

    @Override
    public void isDuplicateMateRequest(Long mateId, Long accountId) {
        if(mateApplyRepository.existsByMateIdAndApplierId(mateId, accountId))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_MATE_REQUEST);
    }

    @Override
    public void saveMateRequestEntity(MateApply mateApply) {
        MateApplyJpaEntity mateRequestEntity = matePersistenceMapper.domainToEntity(mateApply);
        mateApplyRepository.save(mateRequestEntity);
    }

    @Override
    public MateApply loadMateRequestEntity(Long mateId, Long applierId) {
        MateApplyJpaEntity mateRequestEntity = mateApplyRepository.getByMateAndApplier(mateId, applierId);
        return matePersistenceMapper.entityToDomain(mateRequestEntity);
    }

    @Override
    public List<MateRequestSimpleResponse> loadMateRequests(Long applierId, ApproveStatus approveStatus) {
        List<Long> matingIds = mateApplyQueryRepository.getMateIdsFromMateRequest(applierId, approveStatus.name());
        List<MateApplySimpleJpaResponse> jpaResponses = mateQueryRepository.getMyMateRequests(matingIds);
        return matePersistenceMapper.jpaResponsesToResponsesForMateRequest(jpaResponses);
    }

    @Override
    public void deleteAllMateRequestByApplier(AccountId id) {
        mateApplyRepository.deleteAllByApplierId(id.getValue());

    }
}
