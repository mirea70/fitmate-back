package com.fitmate.domain.chat.room;

import lombok.Getter;

@Getter
public class ChatRoomId {
    private final String value;
    public ChatRoomId(String value) {
        this.value = value;
    }
}
