package com.fitmate.port.in.chat.dto;

public record ChatRoomCreateDmCommand(
        Long fromAccountId,
        Long toAccountId
) {
}
