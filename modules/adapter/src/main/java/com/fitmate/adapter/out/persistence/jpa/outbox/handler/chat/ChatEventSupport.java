package com.fitmate.adapter.out.persistence.jpa.outbox.handler.chat;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.chat.enums.MessageType;
import com.fitmate.domain.chat.message.ChatMessage;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.chat.LoadChatPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatEventSupport {

    private static final String ENTER_MESSAGE = "님이 채팅방에 참여했습니다.";
    private static final String LEAVE_MESSAGE = "님이 채팅방을 나갔습니다.";

    private final LoadChatPort loadChatPort;
    private final LoadAccountPort loadAccountPort;

    public boolean existsMateChatRoom(Long mateId) {
        return loadChatPort.existChatRoom(new MateId(mateId));
    }

    public String saveChatRoom(ChatRoom chatRoom) {
        return loadChatPort.saveChatRoom(chatRoom);
    }

    public ChatRoom loadChatRoomByMateId(Long mateId) {
        return loadChatPort.loadChatRoomByMateId(mateId);
    }

    public void saveEnterMessage(String roomId, Long accountId) {
        saveSystemMessage(roomId, accountId, ENTER_MESSAGE, MessageType.ENTER);
    }

    public void saveLeaveMessage(String roomId, Long accountId) {
        saveSystemMessage(roomId, accountId, LEAVE_MESSAGE, MessageType.LEAVE);
    }

    private void saveSystemMessage(String roomId, Long accountId, String suffix, MessageType type) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        String nickName = account.getProfileInfo().getNickName();
        ChatMessage message = ChatMessage.withoutId(roomId, nickName + suffix, accountId, nickName, type, null);
        loadChatPort.saveChatMessage(message);
    }
}
