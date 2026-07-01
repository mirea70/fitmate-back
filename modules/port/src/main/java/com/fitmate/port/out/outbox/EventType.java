package com.fitmate.port.out.outbox;

import com.fitmate.port.out.outbox.payload.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    MATE_REGISTERED(MateRegisteredEventPayload.class, "MATE"),
    MATE_MODIFIED(MateModifiedEventPayload.class, "MATE"),
    MATE_REQUESTED(MateRequestedEventPayload.class, "MATE"),
    MATE_APPROVED(MateApprovedEventPayload.class, "MATE"),
    MATE_CANCELLED(MateCancelledEventPayload.class, "MATE"),
    MATE_AUTO_CANCELLED(MateAutoCancelledEventPayload.class, "MATE"),
    MATE_CLOSED(MateClosedEventPayload.class, "MATE"),
    MATE_REMINDER(MateReminderEventPayload.class, "MATE"),
    FOLLOWED(FollowedEventPayload.class, "ACCOUNT");

    private final Class<? extends EventPayload> payloadClass;
    private final String domainType;
}
