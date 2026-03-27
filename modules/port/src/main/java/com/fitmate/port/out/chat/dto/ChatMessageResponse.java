package com.fitmate.port.out.chat.dto;

import com.fitmate.domain.chat.enums.MessageType;
import lombok.Getter;

@Getter
public class ChatMessageResponse {
    private final String senderNickName;
    private final Long senderId;
    private final String message;
    private final MessageType messageType;

    public ChatMessageResponse(String senderNickName, Long senderId, String message, MessageType messageType) {
        this.senderNickName = senderNickName;
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
    }
}
