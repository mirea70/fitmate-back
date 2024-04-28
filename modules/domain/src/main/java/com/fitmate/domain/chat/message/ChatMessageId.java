package com.fitmate.domain.chat.message;

import lombok.Getter;

@Getter
public class ChatMessageId {
    private final String value;
    public ChatMessageId(String value) {
        this.value = value;
    }
}
