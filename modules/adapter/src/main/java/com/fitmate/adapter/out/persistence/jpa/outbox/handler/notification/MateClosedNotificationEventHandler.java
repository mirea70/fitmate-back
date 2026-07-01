package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateClosedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateClosedNotificationEventHandler")
@RequiredArgsConstructor
public class MateClosedNotificationEventHandler implements OutboxEventHandler<MateClosedEventPayload> {

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateClosedEventPayload> event) {
        MateClosedEventPayload payload = event.getPayload();
        String content = payload.getTitle() + " 모임이 마감되었습니다.";
        for (Long wisherAccountId : payload.getWisherAccountIds()) {
            support.saveNotice(wisherAccountId, payload.getMateId(), payload.getWriterId(), content, NoticeType.MATE_CANCELLED);
        }
    }

    @Override
    public boolean supports(Event<MateClosedEventPayload> event) {
        return event.getType() == EventType.MATE_CLOSED;
    }
}
