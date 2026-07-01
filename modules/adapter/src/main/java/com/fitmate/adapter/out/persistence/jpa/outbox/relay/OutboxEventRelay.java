package com.fitmate.adapter.out.persistence.jpa.outbox.relay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.adapter.out.persistence.jpa.outbox.OutboxEventStatus;
import com.fitmate.adapter.out.persistence.jpa.outbox.entity.OutboxEventJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandlers;
import com.fitmate.adapter.out.persistence.jpa.outbox.repository.OutboxEventRepository;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventPayload;
import com.fitmate.port.out.outbox.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventRelay {

    private static final int MAX_RETRY_COUNT = 3;

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventHandlers outboxEventHandlers;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10, initialDelay = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void publishPendingEvents() {
        List<OutboxEventJpaEntity> events = outboxEventRepository
                .findAllByStatusInAndRetryCountLessThanAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
                        List.of(OutboxEventStatus.PENDING, OutboxEventStatus.FAILED),
                        MAX_RETRY_COUNT,
                        LocalDateTime.now().minusSeconds(10),
                        Pageable.ofSize(100)
                );

        for (OutboxEventJpaEntity event : events) {
            publishEvent(event);
        }
    }

    private void publishEvent(OutboxEventJpaEntity outbox) {
        outbox.markProcessing();
        try {
            EventType eventType = EventType.valueOf(outbox.getEventType());
            EventPayload payload = objectMapper.readValue(outbox.getPayload(), eventType.getPayloadClass());
            outboxEventHandlers.handle(Event.of(eventType, payload));
            outbox.markProcessed();
        } catch (Exception e) {
            outbox.markFailed(e.getMessage());
            log.warn("Outbox event processing failed. eventId={}, eventType={}, retryCount={}",
                    outbox.getId(), outbox.getEventType(), outbox.getRetryCount(), e);
        }
        outboxEventRepository.save(outbox);
    }
}
