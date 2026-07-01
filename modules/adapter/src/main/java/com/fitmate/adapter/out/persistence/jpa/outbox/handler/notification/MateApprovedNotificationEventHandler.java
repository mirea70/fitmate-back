package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateApprovedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateApprovedNotificationEventHandler")
@RequiredArgsConstructor
public class MateApprovedNotificationEventHandler implements OutboxEventHandler<MateApprovedEventPayload> {

    private static final String MESSAGE = " 모집 글에 메이트 신청이 승인되었습니다.";

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateApprovedEventPayload> event) {
        MateApprovedEventPayload payload = event.getPayload();
        support.saveNotice(payload.getApplierId(), payload.getMateId(), null, payload.getTitle() + MESSAGE, NoticeType.MATE_APPROVED);
    }

    @Override
    public boolean supports(Event<MateApprovedEventPayload> event) {
        return event.getType() == EventType.MATE_APPROVED;
    }
}
