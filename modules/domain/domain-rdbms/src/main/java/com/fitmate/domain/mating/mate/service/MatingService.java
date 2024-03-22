package com.fitmate.domain.mating.mate.service;

import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.mate.dto.MyMateRequestsDto;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestQueryRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatingService {
    private final MatingRepository matingRepository;
    private final MateRequestQueryRepository mateRequestQueryRepository;
    private final MatingReadRepository matingReadRepository;

    public Mating validateFindById(Long id) {
        return matingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
    }

    public List<MyMateRequestsDto> findAllMyMateRequest(Long applierId, MateRequest.ApproveStatus approveStatus) {
        List<Long> matingIds = mateRequestQueryRepository.selectMatingIdsForMyMateRequest(applierId, approveStatus);
        return matingReadRepository.getMyMateRequestsQuery(matingIds);
    }
}
