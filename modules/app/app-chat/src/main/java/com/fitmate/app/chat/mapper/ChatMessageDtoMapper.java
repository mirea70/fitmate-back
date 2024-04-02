package com.fitmate.app.chat.mapper;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.domain.mongo.chat.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMessageDtoMapper {

    ChatMessageDtoMapper INSTANCE = Mappers.getMapper(ChatMessageDtoMapper.class);

    ChatMessage toChatMessage(String roomId, ChatMessageDto dto);

    ChatMessageDto toChatMessageDto(ChatMessage chatMessage);

    List<ChatMessageDto> toChatMessageDtoList(List<ChatMessage> chatMessages);
}
