package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.FollowedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("followedNotificationEventHandler")
@RequiredArgsConstructor
public class FollowedNotificationEventHandler implements OutboxEventHandler<FollowedEventPayload> {

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<FollowedEventPayload> event) {
        FollowedEventPayload payload = event.getPayload();
        String content = payload.getFromNickName() + "님이 회원님을 팔로우했습니다.";
        support.saveNotice(payload.getTargetAccountId(), null, payload.getFromAccountId(), content, NoticeType.FOLLOWED);
    }

    @Override
    public boolean supports(Event<FollowedEventPayload> event) {
        return event.getType() == EventType.FOLLOWED;
    }
}
