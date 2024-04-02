package com.fitmate.app.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private String senderNickName;
    private Long senderId;
    private String message;
}
