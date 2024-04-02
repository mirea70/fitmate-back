package com.fitmate.domain.mongo.chat.repository;

import com.fitmate.domain.mongo.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByRoomIdOrderByCreatedAtDesc(String roomId);
}
