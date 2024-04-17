package com.fitmate.app.chat.service;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.app.chat.dto.ChatRoomDto;
import com.fitmate.app.chat.mapper.ChatMessageDtoMapper;
import com.fitmate.app.chat.mapper.ChatRoomDtoMapper;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mongo.chat.dto.ChatRoomListItemDto;
import com.fitmate.domain.mongo.chat.entity.ChatMessage;
import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import com.fitmate.domain.mongo.chat.repository.ChatMessageRepository;
import com.fitmate.domain.mongo.chat.repository.ChatRoomQueryRepository;
import com.fitmate.domain.mongo.chat.repository.ChatRoomRepository;
import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.DuplicatedErrorResult;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomQueryRepository chatRoomQueryRepository;
    private final MatingRepository matingRepository;
    private final AccountRepository accountRepository;
    private static final String DEFAULT_ENTER_MESSAGE = "님이 채팅방에 참여하였습니다.";

    public ChatMessageDto enterChatRoom(String roomId, ChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_CHAT_ROOM_DATA));
        chatRoom.addJoinAccountId(chatMessageDto.getSenderId());
        return getEnterMessageDto(chatMessageDto);
    }

    private ChatMessageDto getEnterMessageDto(ChatMessageDto chatMessageDto) {
        chatMessageDto.setMessage(chatMessageDto.getSenderNickName() + DEFAULT_ENTER_MESSAGE);
        return chatMessageDto;
    }

    public void saveChatMessage(String roomId ,ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = ChatMessageDtoMapper.INSTANCE.toChatMessage(roomId, chatMessageDto);
        chatMessageRepository.save(chatMessage);
    }

    public ChatRoomDto.Response createGroupChatRoom(ChatRoomDto.CreateGroup create) {
        Mating mating = getMatingValidated(create.getMatingId());
        ChatRoom chatRoom = ChatRoomDtoMapper.INSTANCE.toEntity(mating.getTitle(), mating.getId());
        chatRoom.addJoinAccountId(create.getAccountId());
        chatRoomRepository.save(chatRoom);

        return ChatRoomDtoMapper.INSTANCE.toResponse(chatRoom);
    }

    private Mating getMatingValidated(Long matingId) {
        Mating mating = matingRepository.findById(matingId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
        if(chatRoomRepository.existsByMatingId(mating.getId()))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_CHAT_ROOM_ABOUT_MATE);
        return mating;
    }

    public ChatRoomDto.Response createDmChatRoom(ChatRoomDto.CreateDM create) {

        Account fromAccount = accountRepository.findById(create.getFromAccountId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        Account toAccount = accountRepository.findById(create.getToAccountId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        validateDuplicateDmChatRoom(fromAccount.getId(), toAccount.getId());
        ChatRoom chatRoom = ChatRoomDtoMapper.INSTANCE.toEntityByName(fromAccount.getProfileInfo().getNickName());
        chatRoom.addJoinAccountId(fromAccount.getId());
        chatRoom.addJoinAccountId(toAccount.getId());
        chatRoomRepository.save(chatRoom);

        return ChatRoomDtoMapper.INSTANCE.toResponse(chatRoom);
    }

    private void validateDuplicateDmChatRoom(Long fromAccountId, Long toAccountId) {
        if(chatRoomQueryRepository.existJoinAccountIdsContainsAll(Set.of(fromAccountId, toAccountId)))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_CHAT_ROOM_BETWEEN_ACCOUNT);
    }

    public List<ChatMessageDto> getMessagesByRoomId(String roomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return ChatMessageDtoMapper.INSTANCE.toChatMessageDtoList(chatMessages);
    }

//    public List<ChatRoomDto.Response> getMyChatRooms(Long accountId) {
//        List<ChatRoom> chatRooms = chatRoomQueryRepository.findAllByJoinAccountId(accountId);
//        return ChatRoomDtoMapper.INSTANCE.toResponses(chatRooms);
//    }

    public List<ChatRoomListItemDto> getMyChatRooms(Long accountId) {
        List<ChatRoomListItemDto> documents = chatRoomQueryRepository.findAllByMyAccountId(accountId);
        return documents;
    }
}
