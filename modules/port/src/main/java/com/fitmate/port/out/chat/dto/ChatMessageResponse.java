package com.fitmate.port.out.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageResponse {
    private final String senderNickName;
    private final Long senderId;
    private final String message;

    public ChatMessageResponse(String senderNickName, Long senderId, String message) {
        this.senderNickName = senderNickName;
        this.senderId = senderId;
        this.message = message;
    }
}
