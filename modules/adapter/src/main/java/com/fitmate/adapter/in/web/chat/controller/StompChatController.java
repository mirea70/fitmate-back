package com.fitmate.adapter.in.web.chat.controller;

import com.fitmate.adapter.in.web.chat.dto.ChatMessageRequest;
import com.fitmate.adapter.in.web.chat.mapper.ChatWebAdapterMapper;
import com.fitmate.port.in.chat.usecase.ChatUseCasePort;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final ChatUseCasePort chatUseCasePort;
    private final ChatWebAdapterMapper chatWebAdapterMapper;

    @MessageMapping("/{roomId}/enter")
    @SendTo("/sub/{roomId}")
    public ChatMessageResponse enter(@DestinationVariable("roomId") String roomId, ChatMessageRequest request) {
        return chatUseCasePort.enterChatRoom(chatWebAdapterMapper.requestToCommand(request, roomId));
    }

    @MessageMapping("/{roomId}/chat")
    @SendTo("/sub/{roomId}")
    public ChatMessageResponse chat(@DestinationVariable("roomId") String roomId, ChatMessageRequest request) {
        chatUseCasePort.saveChatMessage(chatWebAdapterMapper.requestToCommand(request, roomId));
        return chatWebAdapterMapper.requestToResponse(request);
    }
}
