package com.fitmate.adapter.out.persistence.mongo.chat.repository;

import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatReadStatusMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatReadStatusRepository extends MongoRepository<ChatReadStatusMongoEntity, String> {
}
