package com.fitmate.adapter.out.persistence.jpa.outbox.handler;

import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventHandlers {

    private final List<OutboxEventHandler<? extends EventPayload>> handlers;

    @SuppressWarnings("unchecked")
    public void handle(Event<EventPayload> event) {
        List<OutboxEventHandler<? extends EventPayload>> supportedHandlers = handlers.stream()
                .filter(candidate -> supports(candidate, event))
                .toList();
        if (supportedHandlers.isEmpty()) {
            throw new IllegalArgumentException("Unsupported outbox event type: " + event.getType());
        }

        for (OutboxEventHandler<? extends EventPayload> candidate : supportedHandlers) {
            OutboxEventHandler<EventPayload> handler = (OutboxEventHandler<EventPayload>) candidate;
            handler.handle(event);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean supports(OutboxEventHandler<? extends EventPayload> handler, Event<EventPayload> event) {
        return ((OutboxEventHandler<EventPayload>) handler).supports(event);
    }
}
