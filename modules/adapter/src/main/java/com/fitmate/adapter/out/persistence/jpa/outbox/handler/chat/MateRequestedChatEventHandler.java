package com.fitmate.adapter.out.persistence.jpa.outbox.handler.chat;

import com.fitmate.adapter.out.persistence.jpa.outbox.handler.OutboxEventHandler;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.port.out.outbox.Event;
import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.payload.MateRequestedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("mateRequestedChatEventHandler")
@RequiredArgsConstructor
public class MateRequestedChatEventHandler implements OutboxEventHandler<MateRequestedEventPayload> {

    private final ChatEventSupport support;

    @Override
    public void handle(Event<MateRequestedEventPayload> event) {
        MateRequestedEventPayload payload = event.getPayload();
        if (payload.getApproveStatus() != ApproveStatus.APPROVE) {
            return;
        }

        ChatRoom chatRoom = support.loadChatRoomByMateId(payload.getMateId());
        chatRoom.addJoinAccountId(payload.getApplierId());
        String roomId = support.saveChatRoom(chatRoom);
        support.saveEnterMessage(roomId, payload.getApplierId());
    }

    @Override
    public boolean supports(Event<MateRequestedEventPayload> event) {
        return event.getType() == EventType.MATE_REQUESTED;
    }
}
