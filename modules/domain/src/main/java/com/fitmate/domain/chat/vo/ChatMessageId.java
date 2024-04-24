package com.fitmate.domain.chat.vo;

import lombok.Getter;

@Getter
public class ChatMessageId {
    private final String value;
    public ChatMessageId(String value) {
        this.value = value;
    }
}
