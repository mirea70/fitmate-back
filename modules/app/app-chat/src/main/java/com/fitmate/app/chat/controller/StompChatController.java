package com.fitmate.app.chat.controller;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.app.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final ChatService chatService;

    @MessageMapping("/{roomId}/enter")
    @SendTo("/chat/sub/{roomId}")
    public ChatMessageDto enter(@DestinationVariable("roomId") String roomId, ChatMessageDto chatMessageDto) {
        return chatService.enterChatRoom(roomId, chatMessageDto);
    }

    @MessageMapping("/{roomId}/chat")
    @SendTo("/chat/sub/{roomId}")
    public ChatMessageDto chat(@DestinationVariable("roomId") String roomId, ChatMessageDto chatMessageDto) {
        chatService.saveChatMessage(roomId, chatMessageDto);
        return chatMessageDto;
    }
}
