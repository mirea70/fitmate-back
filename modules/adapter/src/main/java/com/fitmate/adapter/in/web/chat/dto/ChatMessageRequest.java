package com.fitmate.adapter.in.web.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
    private final String senderNickName;
    private final Long senderId;
    private final String message;

    public ChatMessageRequest(String senderNickName, Long senderId, String message) {
        this.senderNickName = senderNickName;
        this.senderId = senderId;
        this.message = message;
    }
}
