package com.fitmate.port.in.chat.dto;

public record ChatMessageCommand(
        String senderNickName,
        Long senderId,
        String message,
        String roomId
) {
}
