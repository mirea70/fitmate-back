package com.fitmate.adapter.out.persistence.jpa.mate.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateQuestionJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateRequestSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateRequestJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.mapper.MatePersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRequestQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRequestRepository;
import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.domain.error.results.DuplicatedErrorResult;
import com.fitmate.domain.mate.aggregate.MateRequest;
import com.fitmate.domain.mate.vo.ApproveStatus;
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
    private final MateRequestRepository mateRequestRepository;
    private final MateRequestQueryRepository mateRequestQueryRepository;

    @Override
    public MateQuestionResponse loadMateQuestion(Long mateId) {
        MateQuestionJpaResponse jpaResponse = mateQueryRepository.readQuestion(mateId);
        return matePersistenceMapper.jpaResponseToResponse(jpaResponse);
    }

    @Override
    public void isDuplicateMateRequest(Long mateId, Long accountId) {
        if(mateRequestRepository.existsByMateIdAndApplierId(mateId, accountId))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_MATE_REQUEST);
    }

    @Override
    public void saveMateRequestEntity(MateRequest mateRequest) {
        MateRequestJpaEntity mateRequestEntity = matePersistenceMapper.domainToEntity(mateRequest);
        mateRequestRepository.save(mateRequestEntity);
    }

    @Override
    public MateRequest loadMateRequestEntity(Long mateId, Long applierId) {
        MateRequestJpaEntity mateRequestEntity = mateRequestRepository.getByMateAndApplier(mateId, applierId);
        return matePersistenceMapper.entityToDomain(mateRequestEntity);
    }

    @Override
    public List<MateRequestSimpleResponse> loadMateRequests(Long applierId, ApproveStatus approveStatus) {
        List<Long> matingIds = mateRequestQueryRepository.getMateIdsFromMateRequest(applierId, approveStatus.name());
        List<MateRequestSimpleJpaResponse> jpaResponses = mateQueryRepository.getMyMateRequests(matingIds);
        return matePersistenceMapper.jpaResponsesToResponsesForMateRequest(jpaResponses);
    }

    @Override
    public void deleteAllMateRequestByApplier(AccountId id) {
        mateRequestRepository.deleteAllByApplierId(id.getValue());

    }
}
