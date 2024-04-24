package com.fitmate.port.in.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageCommand {
    private final String senderNickName;
    private final Long senderId;
    private final String message;
    private final String roomId;

    public ChatMessageCommand(String senderNickName, Long senderId, String message, String roomId) {
        this.senderNickName = senderNickName;
        this.senderId = senderId;
        this.message = message;
        this.roomId = roomId;
    }
}
