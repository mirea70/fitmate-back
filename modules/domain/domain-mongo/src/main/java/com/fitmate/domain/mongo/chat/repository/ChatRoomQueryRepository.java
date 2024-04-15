package com.fitmate.domain.mongo.chat.repository;

import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepository {
    private final MongoTemplate mongoTemplate;

    public boolean existJoinAccountIdsContainsAll(Set<Long> accountIds) {
        Query query = new Query(Criteria.where("joinAccountIds").all(accountIds));
        return mongoTemplate.exists(query, ChatRoom.class);
    }

    public List<ChatRoom> findAllByJoinAccountId(Long accountId) {
        Query query = new Query(Criteria.where("joinAccountIds").in(accountId));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query,ChatRoom.class);
    }
}
