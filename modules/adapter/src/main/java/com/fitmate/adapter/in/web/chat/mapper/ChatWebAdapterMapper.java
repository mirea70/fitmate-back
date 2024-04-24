package com.fitmate.adapter.in.web.chat.mapper;

import com.fitmate.adapter.in.web.chat.dto.ChatMessageRequest;
import com.fitmate.adapter.in.web.chat.dto.ChatRoomCreateDmRequest;
import com.fitmate.adapter.in.web.chat.dto.ChatRoomCreateGroupRequest;
import com.fitmate.port.in.chat.dto.ChatMessageCommand;
import com.fitmate.port.in.chat.dto.ChatRoomCreateDmCommand;
import com.fitmate.port.in.chat.dto.ChatRoomCreateGroupCommand;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatWebAdapterMapper {
    public ChatMessageCommand requestToCommand(ChatMessageRequest request, String roomId) {
        return new ChatMessageCommand(
                request.getSenderNickName(),
                request.getSenderId(),
                request.getMessage(),
                roomId
        );
    }

    public ChatMessageResponse requestToResponse(ChatMessageRequest request) {
        return new ChatMessageResponse(
                request.getSenderNickName(),
                request.getSenderId(),
                request.getMessage()
        );
    }

    public ChatRoomCreateGroupCommand requestToCommand(ChatRoomCreateGroupRequest request) {
        return new ChatRoomCreateGroupCommand(
                request.getMateId(),
                request.getAccountId()
        );
    }

    public ChatRoomCreateDmCommand requestToCommand(ChatRoomCreateDmRequest request) {
        return new ChatRoomCreateDmCommand(
                request.getFromAccountId(),
                request.getToAccountId()
        );
    }
}
