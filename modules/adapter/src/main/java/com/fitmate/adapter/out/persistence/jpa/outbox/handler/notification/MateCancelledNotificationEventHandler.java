package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateCancelledEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateCancelledNotificationEventHandler")
@RequiredArgsConstructor
public class MateCancelledNotificationEventHandler implements OutboxEventHandler<MateCancelledEventPayload> {

    private static final String MESSAGE = "님이 ";
    private static final String CANCELLED_MESSAGE = " 메이트 모집 신청을 취소했습니다.";

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateCancelledEventPayload> event) {
        MateCancelledEventPayload payload = event.getPayload();
        String content = support.nickName(payload.getApplierId()) + MESSAGE + payload.getTitle() + CANCELLED_MESSAGE;
        if (payload.getCancelReason() != null && !payload.getCancelReason().isBlank()) {
            content += " (사유: " + payload.getCancelReason() + ")";
        }
        support.saveNotice(payload.getWriterId(), payload.getMateId(), payload.getApplierId(), content, NoticeType.MATE_CANCELLED);
    }

    @Override
    public boolean supports(Event<MateCancelledEventPayload> event) {
        return event.getType() == EventType.MATE_CANCELLED;
    }
}
