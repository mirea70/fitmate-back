package com.fitmate.adapter.out.persistence.mongo.chat.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatMessageMongoEntity;
import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatReadStatusMongoEntity;
import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatRoomMongoEntity;
import com.fitmate.adapter.out.persistence.mongo.chat.mapper.ChatPersistenceMapper;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatMessageRepository;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatReadStatusRepository;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatRoomQueryRepository;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatRoomRepository;
import com.fitmate.domain.chat.message.ChatMessage;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.domain.mate.MateId;
import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@PersistenceAdapter
@RequiredArgsConstructor
public class ChatPersistenceAdapter implements LoadChatPort {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatPersistenceMapper chatPersistenceMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomQueryRepository chatRoomQueryRepository;
    private final ChatReadStatusRepository chatReadStatusRepository;

    @Override
    public ChatRoom loadChatRoom(String roomId) {
        ChatRoomMongoEntity chatRoomEntity = chatRoomRepository.getById(roomId);
        return chatPersistenceMapper.entityToDomain(chatRoomEntity);
    }

    @Override
    public String saveChatRoom(ChatRoom chatRoom) {
        ChatRoomMongoEntity chatRoomEntity = chatPersistenceMapper.domainToEntity(chatRoom);
        return chatRoomRepository.save(chatRoomEntity).getId();
    }

    @Override
    public void saveChatMessage(ChatMessage chatMessage) {
        ChatMessageMongoEntity chatMessageEntity = chatPersistenceMapper.domainToEntity(chatMessage);
        chatMessageRepository.save(chatMessageEntity);
    }

    @Override
    public boolean existChatRoom(MateId id) {
        return chatRoomRepository.existsByMateId(id.getValue());
    }

    @Override
    public Optional<String> findChatRoomId(Set<Long> accountIds) {
        return chatRoomQueryRepository.findRoomIdByExactJoinAccountIds(accountIds);
    }

    @Override
    public List<ChatMessageResponse> getMessagesByRoomId(String roomId) {
        List<ChatMessageMongoEntity> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId);
        return chatPersistenceMapper.entitiesToResponses(chatMessages);
    }

    @Override
    public List<ChatRoomListItemResponse> getMyChatRooms(Long accountId) {
        return chatRoomQueryRepository.findAllByMyAccountId(accountId);
    }

    @Override
    public void updateReadStatus(String roomId, Long accountId) {
        ChatReadStatusMongoEntity readStatus = new ChatReadStatusMongoEntity(roomId, accountId, new java.util.Date());
        chatReadStatusRepository.save(readStatus);
    }
}
