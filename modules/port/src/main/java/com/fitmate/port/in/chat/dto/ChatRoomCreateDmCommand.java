package com.fitmate.port.in.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomCreateDmCommand {
    private final Long fromAccountId;
    private final Long toAccountId;

    public ChatRoomCreateDmCommand(Long fromAccountId, Long toAccountId) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }
}
