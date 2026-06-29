package com.fitmate.port.out.chat.dto;

import java.util.Date;
import java.util.List;

public record ChatRoomListItemResponse(
        String roomId,
        String roomName,
        String lastMessage,
        Date lastMessageTime,
        Long matingId,
        String roomType,
        List<Long> memberAccountIds,
        int unreadCount
) {
}
