package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateReminderEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateReminderNotificationEventHandler")
@RequiredArgsConstructor
public class MateReminderNotificationEventHandler implements OutboxEventHandler<MateReminderEventPayload> {

    private static final String MESSAGE = " 모임이 내일 예정되어 있습니다.";

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateReminderEventPayload> event) {
        MateReminderEventPayload payload = event.getPayload();
        support.saveNotice(payload.getAccountId(), payload.getMateId(), null, payload.getTitle() + MESSAGE, NoticeType.MATE_REMINDER);
    }

    @Override
    public boolean supports(Event<MateReminderEventPayload> event) {
        return event.getType() == EventType.MATE_REMINDER;
    }
}
