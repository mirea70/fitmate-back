package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateRequestedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateRequestedNotificationEventHandler")
@RequiredArgsConstructor
public class MateRequestedNotificationEventHandler implements OutboxEventHandler<MateRequestedEventPayload> {

    private static final String REQUEST_MESSAGE = " 모집 글에 새로운 메이트 신청이 있습니다.";
    private static final String APPROVE_MESSAGE = " 모집 글에 메이트 신청이 승인되었습니다.";

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateRequestedEventPayload> event) {
        MateRequestedEventPayload payload = event.getPayload();
        String requestContent = payload.getTitle() + REQUEST_MESSAGE;
        support.saveNotice(payload.getWriterId(), payload.getMateId(), payload.getApplierId(), requestContent, NoticeType.MATE_REQUESTED);

        if (payload.getApproveStatus() == ApproveStatus.APPROVE) {
            String approveContent = payload.getTitle() + APPROVE_MESSAGE;
            support.saveNotice(payload.getApplierId(), payload.getMateId(), null, approveContent, NoticeType.MATE_APPROVED);
        }
    }

    @Override
    public boolean supports(Event<MateRequestedEventPayload> event) {
        return event.getType() == EventType.MATE_REQUESTED;
    }
}
