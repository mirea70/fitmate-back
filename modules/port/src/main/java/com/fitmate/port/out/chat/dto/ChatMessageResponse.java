package com.fitmate.port.out.chat.dto;

import com.fitmate.domain.chat.enums.MessageType;

public record ChatMessageResponse(
        String senderNickName,
        Long senderId,
        String message,
        MessageType messageType
) {
}
