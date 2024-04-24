package com.fitmate.port.out.chat.dto;

import com.fitmate.domain.chat.vo.RoomType;
import lombok.Getter;

@Getter
public class ChatRoomResponse {
    private final String id;
    private final String name;
    private final Long mateId;
    private final RoomType roomType;

    public ChatRoomResponse(String id, String name, Long mateId, RoomType roomType) {
        this.id = id;
        this.name = name;
        this.mateId = mateId;
        this.roomType = roomType;
    }
}
