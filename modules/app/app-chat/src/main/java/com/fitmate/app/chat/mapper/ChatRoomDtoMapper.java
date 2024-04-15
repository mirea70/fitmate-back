package com.fitmate.app.chat.mapper;

import com.fitmate.app.chat.dto.ChatRoomDto;
import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomDtoMapper {
    ChatRoomDtoMapper INSTANCE = Mappers.getMapper(ChatRoomDtoMapper.class);

    ChatRoomDto.Response toResponse(ChatRoom chatRoom);
    List<ChatRoomDto.Response> toResponses(List<ChatRoom> chatRooms);
    ChatRoom toEntity(String name, Long matingId);
    ChatRoom toEntityByName(String name);
}
