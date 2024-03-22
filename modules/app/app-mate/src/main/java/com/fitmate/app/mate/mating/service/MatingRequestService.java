package com.fitmate.app.mate.mating.service;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.event.MateApproveEvent;
import com.fitmate.app.mate.mating.event.MateRequestEvent;
import com.fitmate.app.mate.mating.mapper.MateEventDtoMapper;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.enums.GatherType;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestQueryRepository;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.DuplicatedErrorResult;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class MatingRequestService {

    private final MateRequestRepository mateRequestRepository;

    private final MatingRepository matingRepository;

    private final MateRequestQueryRepository mateRequestQueryRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final EntityManager em;

    public Long matingRequest(MatingDto.Apply applyDto) {

        if(mateRequestRepository.existsByMatingIdAndAccountId(applyDto.getMatingId(), applyDto.getAccountId()))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_MATE_REQUEST);

        Mating mating = matingRepository.findById(applyDto.getMatingId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
        applyDto.setApproveStatus(mating.getGatherType() == GatherType.AGREE ? MateRequest.ApproveStatus.WAIT
                : MateRequest.ApproveStatus.APPROVE);

        MateRequest mateRequest = MatingDtoMapper.INSTANCE.applyToEntity(applyDto);
        MateRequest savedMateRequest = mateRequestRepository.save(mateRequest);

        afterInsert(mating, applyDto.getAccountId());

        return savedMateRequest.getId();
    }

    private void afterInsert(Mating mating, Long accountId) {

        mating.addWaitingAccountId(accountId);

        /**
         * [이벤트 처리 목록]
         * 1. 메이트 신청자 알림 보내기
         * 2. 메이트 신청자 휴대폰 문자 보내기
         */
        MateEventDto.Request event = MateEventDtoMapper.INSTANCE.toEvent(mating.getTitle(), accountId);
        MateRequestEvent mateRequestEvent = new MateRequestEvent(event);
        eventPublisher.publishEvent(mateRequestEvent);
    }

    public void approveRequest(MatingDto.Approve approveDto) {
        Mating mating = matingRepository.findById(approveDto.getMatingId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
        Set<Long> accountIds = approveDto.getAccountIds();
        mateRequestQueryRepository.approveAccountIds(approveDto.getMatingId(), accountIds);

        mating.forApproveRequest(accountIds);
        em.flush();
        em.clear();

        afterApprove(mating, accountIds);
    }

    private void afterApprove(Mating mating, Set<Long> accountIds) {

        /**
         * [이벤트 처리 목록]
         * 1. 메이트 신청자 알림 보내기
         * 2. 메이트 신청자 휴대폰 문자 보내기 (구현 필요)
         */

        MateEventDto.Approve event = MateEventDtoMapper.INSTANCE.toEvent(mating.getTitle(), accountIds);
        MateApproveEvent mateApproveEvent = new MateApproveEvent(event);
        eventPublisher.publishEvent(mateApproveEvent);
    }
}
