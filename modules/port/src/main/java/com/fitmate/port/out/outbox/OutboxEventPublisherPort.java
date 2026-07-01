package com.fitmate.port.out.outbox;

public interface OutboxEventPublisherPort {
    void publish(EventType type, Long domainId, EventPayload payload);
}
