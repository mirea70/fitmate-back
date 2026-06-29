package com.fitmate.port.out.chat.dto;

import com.fitmate.domain.chat.enums.RoomType;

public record ChatRoomResponse(
        String id,
        String name,
        Long mateId,
        RoomType roomType
) {
}
