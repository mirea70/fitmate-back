package com.fitmate.port.out.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomSimpleResponse {
    private final String roomId;

    public ChatRoomSimpleResponse(String roomId) {
        this.roomId = roomId;
    }
}
