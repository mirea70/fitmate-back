package com.fitmate.port.in.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomCreateGroupCommand {
    private final Long mateId;
    private final Long accountId;

    public ChatRoomCreateGroupCommand(Long mateId, Long accountId) {
        this.mateId = mateId;
        this.accountId = accountId;
    }
}
