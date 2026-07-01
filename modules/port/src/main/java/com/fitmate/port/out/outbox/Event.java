package com.fitmate.port.out.outbox;

import lombok.Getter;

@Getter
public class Event<T extends EventPayload> {
    private EventType type;
    private T payload;

    public static Event<EventPayload> of(EventType type, EventPayload payload) {
        Event<EventPayload> event = new Event<>();
        event.type = type;
        event.payload = payload;
        return event;
    }
}
