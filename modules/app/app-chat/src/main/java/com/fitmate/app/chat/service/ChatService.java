package com.fitmate.app.chat.service;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.app.chat.dto.ChatRoomDto;
import com.fitmate.app.chat.mapper.ChatMessageDtoMapper;
import com.fitmate.app.chat.mapper.ChatRoomDtoMapper;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mongo.chat.entity.ChatMessage;
import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import com.fitmate.domain.mongo.chat.repository.ChatMessageRepository;
import com.fitmate.domain.mongo.chat.repository.ChatRoomRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MatingRepository matingRepository;
    private static final String DEFAULT_ENTER_MESSAGE = "님이 채팅방에 참여하였습니다.";

    public ChatMessageDto getEnterMessageDto(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessage(chatMessageDto.getSenderNickName() + DEFAULT_ENTER_MESSAGE);
        return chatMessageDto;
    }

    public void saveChatMessage(String roomId ,ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = ChatMessageDtoMapper.INSTANCE.toChatMessage(roomId, chatMessageDto);
        chatMessageRepository.save(chatMessage);
    }

    @Transactional(readOnly = true)
    public ChatRoomDto.Response createGroupChatRoom(ChatRoomDto.CreateGroup createGroup) {
        Mating mating = matingRepository.findById(createGroup.getMatingId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoomDtoMapper.INSTANCE.toEntity(mating.getTitle(), mating.getId()));

        return ChatRoomDtoMapper.INSTANCE.toResponse(chatRoom);
    }

    public List<ChatMessageDto> getMessagesBtRoomId(String roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return ChatMessageDtoMapper.INSTANCE.toChatMessageDtoList(chatMessages);
    }
}
