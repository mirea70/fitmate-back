package com.fitmate.adapter.out.persistence.jpa.outbox.handler.chat;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateApprovedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateApprovedChatEventHandler")
@RequiredArgsConstructor
public class MateApprovedChatEventHandler implements OutboxEventHandler<MateApprovedEventPayload> {

    private final ChatEventSupport support;

    @Override
    public void handle(Event<MateApprovedEventPayload> event) {
        MateApprovedEventPayload payload = event.getPayload();
        ChatRoom chatRoom = support.loadChatRoomByMateId(payload.getMateId());
        chatRoom.addJoinAccountId(payload.getApplierId());
        String roomId = support.saveChatRoom(chatRoom);
        support.saveEnterMessage(roomId, payload.getApplierId());
    }

    @Override
    public boolean supports(Event<MateApprovedEventPayload> event) {
        return event.getType() == EventType.MATE_APPROVED;
    }
}
