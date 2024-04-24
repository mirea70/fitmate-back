package com.fitmate.adapter.out.persistence.mongo.chat.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatMessageMongoEntity;
import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatRoomMongoEntity;
import com.fitmate.adapter.out.persistence.mongo.chat.mapper.ChatPersistenceMapper;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatMessageRepository;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatRoomQueryRepository;
import com.fitmate.adapter.out.persistence.mongo.chat.repository.ChatRoomRepository;
import com.fitmate.domain.chat.aggregate.ChatMessage;
import com.fitmate.domain.chat.aggregate.ChatRoom;
import com.fitmate.domain.mate.vo.MateId;
import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@PersistenceAdapter
@RequiredArgsConstructor
public class ChatPersistenceAdapter implements LoadChatPort {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatPersistenceMapper chatPersistenceMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomQueryRepository chatRoomQueryRepository;

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
    public boolean existChatRoom(Set<Long> accountIds) {
        return chatRoomQueryRepository.existJoinAccountIdsContainsAll(accountIds);
    }

    @Override
    public List<ChatMessageResponse> getMessagesByRoomId(String roomId) {
        List<ChatMessageMongoEntity> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return chatPersistenceMapper.entitiesToResponses(chatMessages);
    }

    @Override
    public List<ChatRoomListItemResponse> getMyChatRooms(Long accountId) {
        return chatRoomQueryRepository.findAllByMyAccountId(accountId);
    }
}
