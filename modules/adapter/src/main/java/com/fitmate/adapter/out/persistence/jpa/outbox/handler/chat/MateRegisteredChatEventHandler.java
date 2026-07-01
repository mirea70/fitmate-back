package com.fitmate.adapter.out.persistence.jpa.outbox.handler.chat;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.chat.enums.RoomType;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateRegisteredEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateRegisteredChatEventHandler")
@RequiredArgsConstructor
public class MateRegisteredChatEventHandler implements OutboxEventHandler<MateRegisteredEventPayload> {

    private final ChatEventSupport support;

    @Override
    public void handle(Event<MateRegisteredEventPayload> event) {
        MateRegisteredEventPayload payload = event.getPayload();
        if (support.existsMateChatRoom(payload.getMateId())) {
            return;
        }

        ChatRoom chatRoom = ChatRoom.withoutId(payload.getTitle(), payload.getMateId(), null, null, RoomType.GROUP);
        chatRoom.addJoinAccountId(payload.getWriterId());
        String roomId = support.saveChatRoom(chatRoom);
        support.saveEnterMessage(roomId, payload.getWriterId());
    }

    @Override
    public boolean supports(Event<MateRegisteredEventPayload> event) {
        return event.getType() == EventType.MATE_REGISTERED;
    }
}
