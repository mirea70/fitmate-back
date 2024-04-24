package com.fitmate.usecase.chat.mapper;

import com.fitmate.domain.chat.aggregate.ChatMessage;
import com.fitmate.domain.chat.aggregate.ChatRoom;
import com.fitmate.domain.chat.vo.RoomType;
import com.fitmate.port.in.chat.dto.ChatMessageCommand;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatUseCaseMapper {
    public ChatMessageResponse commandToResponse(ChatMessageCommand command, String messageDefault) {
        String nickName = command.getSenderNickName();
        return new ChatMessageResponse(
                nickName,
                command.getSenderId(),
                nickName + messageDefault
        );
    }

    public ChatMessage commandToDomain(ChatMessageCommand command) {
        return ChatMessage.withoutId(
                command.getRoomId(),
                command.getMessage(),
                command.getSenderId(),
                command.getSenderNickName(),
                null
        );
    }

    public ChatRoom commandToDomain(String mateTitle, Long mateId) {
        return ChatRoom.withoutId(
                mateTitle,
                mateId,
                null,
                null,
                RoomType.GROUP
        );
    }
}
