package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateRegisteredEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateRegisteredNotificationEventHandler")
@RequiredArgsConstructor
public class MateRegisteredNotificationEventHandler implements OutboxEventHandler<MateRegisteredEventPayload> {

    private static final String MESSAGE = "님이 ";
    private static final String REGISTERED_MESSAGE = " 메이트 모집을 시작했습니다.";

    private final NotificationEventSupport support;

    @Override
    public void handle(Event<MateRegisteredEventPayload> event) {
        MateRegisteredEventPayload payload = event.getPayload();
        String writerNickName = support.nickName(payload.getWriterId());

        for (Long followerId : support.followerIds(payload.getWriterId())) {
            String content = writerNickName + MESSAGE + payload.getTitle() + REGISTERED_MESSAGE;
            support.saveNotice(followerId, payload.getMateId(), payload.getWriterId(), content, NoticeType.MATE_REGISTERED);
        }
    }

    @Override
    public boolean supports(Event<MateRegisteredEventPayload> event) {
        return event.getType() == EventType.MATE_REGISTERED;
    }
}
