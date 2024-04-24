package com.fitmate.port.out.chat;

import com.fitmate.domain.chat.aggregate.ChatMessage;
import com.fitmate.domain.chat.aggregate.ChatRoom;
import com.fitmate.domain.mate.vo.MateId;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;

import java.util.List;
import java.util.Set;

public interface LoadChatPort {
    ChatRoom loadChatRoom(String roomId);
    String saveChatRoom(ChatRoom chatRoom);
    void saveChatMessage(ChatMessage chatMessage);
    boolean existChatRoom(MateId id);
    boolean existChatRoom(Set<Long> accountIds);
    List<ChatMessageResponse> getMessagesByRoomId(String roomId);
    List<ChatRoomListItemResponse> getMyChatRooms(Long accountId);
}
