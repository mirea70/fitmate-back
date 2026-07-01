package com.fitmate.adapter.out.persistence.jpa.outbox.handler.chat;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateCancelledEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateCancelledChatEventHandler")
@RequiredArgsConstructor
public class MateCancelledChatEventHandler implements OutboxEventHandler<MateCancelledEventPayload> {

    private final ChatEventSupport support;

    @Override
    public void handle(Event<MateCancelledEventPayload> event) {
        MateCancelledEventPayload payload = event.getPayload();
        if (!payload.isWasApproved()) {
            return;
        }

        ChatRoom chatRoom = support.loadChatRoomByMateId(payload.getMateId());
        chatRoom.removeJoinAccountId(payload.getApplierId());
        String roomId = support.saveChatRoom(chatRoom);
        support.saveLeaveMessage(roomId, payload.getApplierId());
    }

    @Override
    public boolean supports(Event<MateCancelledEventPayload> event) {
        return event.getType() == EventType.MATE_CANCELLED;
    }
}
