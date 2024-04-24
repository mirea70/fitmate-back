package com.fitmate.port.out.chat.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class ChatRoomListItemResponse {
    private String roomId;
    private String roomName;
    private String lastMessage;
    private Date lastMessageTime;
    private Long matingId;
    private String roomType;
}
