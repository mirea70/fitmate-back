package com.fitmate.app.mate.mating.service;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.event.MateRequestEvent;
import com.fitmate.app.mate.mating.mapper.MateEventDtoMapper;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatingRequestService {

    private final MateRequestRepository mateRequestRepository;

    private final MatingRepository matingRepository;

    private final ApplicationEventPublisher eventPublisher;

    public Long matingRequest(MatingDto.Apply applyDto, Long accountId) {
        MateRequest mateRequest = MatingDtoMapper.INSTANCE.applyToEntity(applyDto);
        MateRequest savedMateRequest = mateRequestRepository.save(mateRequest);

        afterInsert(applyDto.getMatingId(), accountId);

        return savedMateRequest.getId();
    }

    private void afterInsert(Long matingId, Long accountId) {
        addMatingWaitingList(matingId, accountId);
        executeNoticeEvent(matingId, accountId);
    }

    private void addMatingWaitingList(Long matingId, Long accountId) {
        Mating mating = matingRepository.findById(matingId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
        mating.addWaitingAccountId(accountId);
    }

    private void executeNoticeEvent(Long matingId, Long accountId) {
        MateEventDto.Request event = MateEventDtoMapper.INSTANCE.toEvent(matingId, accountId);
        MateRequestEvent mateRequestEvent = new MateRequestEvent(event);
        eventPublisher.publishEvent(mateRequestEvent);
    }
}
