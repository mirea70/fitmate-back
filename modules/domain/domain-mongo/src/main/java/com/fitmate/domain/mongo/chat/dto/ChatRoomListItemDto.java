package com.fitmate.domain.mongo.chat.dto;

import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import lombok.*;

import java.util.Date;

@Getter
public class ChatRoomListItemDto {
    private String roomId;
    private String roomName;
    private String lastMessage;
    private Date lastMessageTime;
    private Long matingId;
    private ChatRoom.RoomType roomType;
}
