package com.fitmate.port.in.chat.usecase;

import com.fitmate.port.in.chat.dto.ChatMessageCommand;
import com.fitmate.port.in.chat.dto.ChatRoomCreateDmCommand;
import com.fitmate.port.in.chat.dto.ChatRoomCreateGroupCommand;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;
import com.fitmate.port.out.chat.dto.ChatRoomSimpleResponse;

import java.util.List;

public interface ChatUseCasePort {
    ChatMessageResponse enterChatRoom(ChatMessageCommand chatMessageCommand);
    void saveChatMessage(ChatMessageCommand chatMessageCommand);
    ChatRoomSimpleResponse createGroupChatRoom(ChatRoomCreateGroupCommand createGroupCommand);
    ChatRoomSimpleResponse createDmChatRoom(ChatRoomCreateDmCommand createDmCommand);
    List<ChatMessageResponse> getMessages(String roomId);
    List<ChatRoomListItemResponse> getMyChatRooms(Long accountId);
}
