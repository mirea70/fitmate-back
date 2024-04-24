package com.fitmate.adapter.out.persistence.mongo.chat.repository;

import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatMessageMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageMongoEntity, String> {
    List<ChatMessageMongoEntity> findAllByRoomIdOrderByCreatedAtDesc(String roomId);
}
