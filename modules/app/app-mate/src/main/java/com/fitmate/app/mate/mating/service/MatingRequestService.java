package com.fitmate.app.mate.mating.service;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.event.MateRequestEvent;
import com.fitmate.app.mate.mating.mapper.MateEventDtoMapper;
import com.fitmate.app.mate.mating.mapper.MatingDtoMapper;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatingRequestService {

    private final MateRequestRepository mateRequestRepository;

    private final ApplicationEventPublisher eventPublisher;

    public Long matingRequest(MatingDto.Apply applyDto, Long accountId) {
        MateRequest mateRequest = MatingDtoMapper.INSTANCE.applyToEntity(applyDto);
        MateRequest savedMateRequest = mateRequestRepository.save(mateRequest);

        MateEventDto.Request event = MateEventDtoMapper.INSTANCE.toEvent(applyDto.getMatingId(), accountId);
        MateRequestEvent mateRequestEvent = new MateRequestEvent(event);
        eventPublisher.publishEvent(mateRequestEvent);

        return savedMateRequest.getId();
    }
}
