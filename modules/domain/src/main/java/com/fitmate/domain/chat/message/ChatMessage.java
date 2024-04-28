package com.fitmate.domain.chat.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMessage {

    private ChatMessageId id;

    private String roomId;

    private String message;

    private Long senderId;

    private String senderNickName;

    private Date createdAt;

    private Integer version;

    public static ChatMessage withId(ChatMessageId id, String roomId, String message, Long senderId, String senderNickName, Date createdAt, Integer version) {
        return new ChatMessage(
                id,
                roomId,
                message,
                senderId,
                senderNickName,
                createdAt,
                version
        );
    }

    public static ChatMessage withoutId(String roomId, String message, Long senderId, String senderNickName, Date createdAt) {
        return new ChatMessage(
                null,
                roomId,
                message,
                senderId,
                senderNickName,
                createdAt,
                null
        );
    }
}
