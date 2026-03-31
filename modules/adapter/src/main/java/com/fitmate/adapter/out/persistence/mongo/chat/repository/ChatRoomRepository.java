package com.fitmate.adapter.out.persistence.mongo.chat.repository;

import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatRoomMongoEntity;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoomMongoEntity, String> {
    boolean existsByMateId(Long matingId);

    default ChatRoomMongoEntity getByMateId(Long mateId) {
        return this.findByMateId(mateId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_CHAT_ROOM_DATA));
    }

    java.util.Optional<ChatRoomMongoEntity> findByMateId(Long mateId);

    default ChatRoomMongoEntity getById(String roomId) {
        return this.findById(roomId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_CHAT_ROOM_DATA));
    }
}
