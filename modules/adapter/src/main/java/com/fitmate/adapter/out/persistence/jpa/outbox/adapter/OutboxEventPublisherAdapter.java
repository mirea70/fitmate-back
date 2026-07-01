package com.fitmate.adapter.out.persistence.jpa.outbox.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.outbox.entity.OutboxEventJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandlers;
import com.fitmate.adapter.out.persistence.jpa.outbox.repository.OutboxEventRepository;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventPayload;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.OutboxEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PersistenceAdapter
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisherAdapter implements OutboxEventPublisherPort {

    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventHandlers outboxEventHandlers;

    @Override
    public void publish(EventType type, Long domainId, EventPayload payload) {
        String serializedPayload = writePayload(payload);
        publishImmediatelyOrSaveOutbox(type, domainId, payload, serializedPayload);
    }

    private void publishImmediatelyOrSaveOutbox(EventType type, Long domainId, EventPayload payload, String serializedPayload) {
        try {
            outboxEventHandlers.handle(Event.of(type, payload));
        } catch (Exception e) {
            log.warn("Immediate outbox event handling failed. eventType={}, domainId={}", type, domainId, e);
            saveOutbox(type, domainId, serializedPayload);
        }
    }

    private void saveOutbox(EventType type, Long domainId, String serializedPayload) {
        outboxEventRepository.save(new OutboxEventJpaEntity(
                type.name(),
                type.getDomainType(),
                domainId,
                serializedPayload
        ));
    }

    private String writePayload(EventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize outbox event payload", e);
        }
    }
}
