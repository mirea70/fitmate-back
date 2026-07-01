package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateAutoCancelledEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateAutoCancelledNotificationEventHandler")
@RequiredArgsConstructor
public class MateAutoCancelledNotificationEventHandler implements OutboxEventHandler<MateAutoCancelledEventPayload> {

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateAutoCancelledEventPayload> event) {
        MateAutoCancelledEventPayload payload = event.getPayload();
        String content = payload.getTitle() + " 모임 신청이 자동 취소되었습니다.";
        if (payload.getCancelReason() != null && !payload.getCancelReason().isBlank()) {
            content += " (사유: " + payload.getCancelReason() + ")";
        }
        support.saveNotice(payload.getApplierId(), payload.getMateId(), payload.getWriterId(), content, NoticeType.MATE_CANCELLED);
    }

    @Override
    public boolean supports(Event<MateAutoCancelledEventPayload> event) {
        return event.getType() == EventType.MATE_AUTO_CANCELLED;
    }
}
