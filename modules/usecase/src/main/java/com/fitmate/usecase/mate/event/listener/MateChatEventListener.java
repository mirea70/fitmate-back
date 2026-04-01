package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.chat.enums.MessageType;
import com.fitmate.domain.chat.enums.RoomType;
import com.fitmate.domain.chat.message.ChatMessage;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.usecase.mate.event.MateApproveEvent;
import com.fitmate.usecase.mate.event.MateCancelledEvent;
import com.fitmate.usecase.mate.event.MateRegisteredEvent;
import com.fitmate.usecase.mate.event.MateRequestEvent;
import com.fitmate.usecase.mate.event.dto.MateApproveEventDto;
import com.fitmate.usecase.mate.event.dto.MateCancelledEventDto;
import com.fitmate.usecase.mate.event.dto.MateRegisteredEventDto;
import com.fitmate.usecase.mate.event.dto.MateRequestEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MateChatEventListener {

    private final LoadChatPort loadChatPort;
    private final LoadAccountPort loadAccountPort;
    private static final String ENTER_MESSAGE = "님이 채팅방에 참여하였습니다.";
    private static final String LEAVE_MESSAGE = "님이 채팅방을 나갔습니다.";

    @EventListener
    @Order(1)
    public void handleMateRegistered(MateRegisteredEvent event) {
        MateRegisteredEventDto dto = event.getEventDto();

        ChatRoom chatRoom = ChatRoom.withoutId(dto.getTitle(), dto.getMateId(), null, null, RoomType.GROUP);
        chatRoom.addJoinAccountId(dto.getWriterId());
        String roomId = loadChatPort.saveChatRoom(chatRoom);

        saveEnterMessage(roomId, dto.getWriterId());
    }

    @EventListener
    @Order(1)
    public void handleMateRequest(MateRequestEvent event) {
        MateRequestEventDto dto = event.getEventDto();
        if (dto.getApproveStatus() != ApproveStatus.APPROVE) return;

        addToChatRoom(dto.getMateId(), dto.getApplierId());
    }

    @EventListener
    @Order(1)
    public void handleMateApprove(MateApproveEvent event) {
        MateApproveEventDto dto = event.getEventDto();
        addToChatRoom(dto.getMateId(), dto.getApplierId());
    }

    @EventListener
    public void handleMateCancelled(MateCancelledEvent event) {
        MateCancelledEventDto dto = event.getEventDto();
        if (!dto.isWasApproved()) return;

        removeFromChatRoom(dto.getMateId(), dto.getApplierId());
    }

    private void addToChatRoom(Long mateId, Long accountId) {
        ChatRoom chatRoom = loadChatPort.loadChatRoomByMateId(mateId);
        chatRoom.addJoinAccountId(accountId);
        String roomId = loadChatPort.saveChatRoom(chatRoom);
        saveEnterMessage(roomId, accountId);
    }

    private void removeFromChatRoom(Long mateId, Long accountId) {
        ChatRoom chatRoom = loadChatPort.loadChatRoomByMateId(mateId);
        chatRoom.removeJoinAccountId(accountId);
        String roomId = loadChatPort.saveChatRoom(chatRoom);
        saveLeaveMessage(roomId, accountId);
    }

    private void saveEnterMessage(String roomId, Long accountId) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        String nickName = account.getProfileInfo().getNickName();
        ChatMessage message = ChatMessage.withoutId(roomId, nickName + ENTER_MESSAGE, accountId, nickName, MessageType.ENTER, null);
        loadChatPort.saveChatMessage(message);
    }

    private void saveLeaveMessage(String roomId, Long accountId) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        String nickName = account.getProfileInfo().getNickName();
        ChatMessage message = ChatMessage.withoutId(roomId, nickName + LEAVE_MESSAGE, accountId, nickName, MessageType.LEAVE, null);
        loadChatPort.saveChatMessage(message);
    }
}
