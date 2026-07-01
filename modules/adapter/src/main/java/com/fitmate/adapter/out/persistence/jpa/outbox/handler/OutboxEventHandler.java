package com.fitmate.adapter.out.persistence.jpa.outbox.handler;

import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventPayload;

public interface OutboxEventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
