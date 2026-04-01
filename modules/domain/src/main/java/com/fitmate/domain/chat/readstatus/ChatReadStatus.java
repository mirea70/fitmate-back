package com.fitmate.domain.chat.readstatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatReadStatus {

    private final String roomId;
    private final Long accountId;
    private final Date lastReadAt;

    public static ChatReadStatus of(String roomId, Long accountId) {
        return new ChatReadStatus(roomId, accountId, new Date());
    }
}
