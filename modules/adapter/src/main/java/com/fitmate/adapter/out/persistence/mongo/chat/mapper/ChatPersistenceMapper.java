package com.fitmate.adapter.out.persistence.mongo.chat.mapper;

import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatMessageMongoEntity;
import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatRoomMongoEntity;
import com.fitmate.domain.chat.aggregate.ChatMessage;
import com.fitmate.domain.chat.aggregate.ChatRoom;
import com.fitmate.domain.chat.vo.ChatRoomId;
import com.fitmate.domain.chat.vo.RoomType;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatPersistenceMapper {
    public ChatRoom entityToDomain(ChatRoomMongoEntity entity) {
        return ChatRoom.withId(
                new ChatRoomId(entity.getId()),
                entity.getName(),
                entity.getMateId(),
                entity.getCreatedAt(),
                entity.getJoinAccountIds(),
                RoomType.valueOf(entity.getRoomType()),
                entity.getVersion()
        );
    }

    public ChatRoomMongoEntity domainToEntity(ChatRoom chatRoom) {
        return new ChatRoomMongoEntity(
                chatRoom.getId() != null ? chatRoom.getId().getValue() : null,
                chatRoom.getName(),
                chatRoom.getMateId(),
                chatRoom.getCreatedAt(),
                chatRoom.getJoinAccountIds(),
                chatRoom.getVersion(),
                chatRoom.getRoomType().name()
        );
    }

    public ChatMessageMongoEntity domainToEntity(ChatMessage chatMessage) {
        return new ChatMessageMongoEntity(
                chatMessage.getId() != null ? chatMessage.getId().getValue() : null,
                chatMessage.getRoomId(),
                chatMessage.getMessage(),
                chatMessage.getSenderId(),
                chatMessage.getSenderNickName(),
                chatMessage.getCreatedAt(),
                chatMessage.getVersion()
        );
    }

    public ChatMessageResponse entityToResponse(ChatMessageMongoEntity entity) {
        return new ChatMessageResponse(
                entity.getSenderNickName(),
                entity.getSenderId(),
                entity.getMessage()
        );
    }

    public List<ChatMessageResponse> entitiesToResponses(List<ChatMessageMongoEntity> entities) {
        return entities.stream()
                .map(this::entityToResponse)
                .toList();
    }
}
