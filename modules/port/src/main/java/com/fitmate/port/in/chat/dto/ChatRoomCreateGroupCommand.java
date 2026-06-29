package com.fitmate.port.in.chat.dto;

public record ChatRoomCreateGroupCommand(
        Long mateId,
        Long accountId
) {
}
