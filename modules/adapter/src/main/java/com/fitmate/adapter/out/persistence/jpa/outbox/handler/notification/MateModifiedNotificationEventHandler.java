package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateModifiedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateModifiedNotificationEventHandler")
@RequiredArgsConstructor
public class MateModifiedNotificationEventHandler implements OutboxEventHandler<MateModifiedEventPayload> {

    private static final String MESSAGE = " 모집 글의 정보가 수정되었습니다.";

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateModifiedEventPayload> event) {
        MateModifiedEventPayload payload = event.getPayload();
        String content = payload.getTitle() + MESSAGE;
        for (Long wisherId : support.wisherAccountIds(payload.getMateId())) {
            support.saveNotice(wisherId, payload.getMateId(), null, content, NoticeType.MATE_MODIFIED);
        }
    }

    @Override
    public boolean supports(Event<MateModifiedEventPayload> event) {
        return event.getType() == EventType.MATE_MODIFIED;
    }
}
