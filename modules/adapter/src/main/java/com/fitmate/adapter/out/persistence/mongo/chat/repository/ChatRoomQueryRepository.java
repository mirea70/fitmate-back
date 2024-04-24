package com.fitmate.adapter.out.persistence.mongo.chat.repository;

import com.fitmate.adapter.out.persistence.mongo.chat.entity.ChatRoomMongoEntity;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
        return mongoTemplate.exists(query, ChatRoomMongoEntity.class);
    }

    public List<ChatRoomListItemResponse> findAllByMyAccountId(Long accountId) {
        Aggregation aggregation = getAggregationForFindAllByMyAccountId(accountId);

        AggregationResults<ChatRoomListItemResponse> results = mongoTemplate.aggregate(aggregation, "ChatMessage", ChatRoomListItemResponse.class);
        return results.getMappedResults();
    }

    private Aggregation getAggregationForFindAllByMyAccountId(Long accountId) {
        AggregationOperation lookupOperation = Aggregation.lookup(
                "ChatRoom", "roomId", "_id", "room"
        );

        AggregationOperation unwindOperation = Aggregation.unwind("room");

        AggregationOperation matchOperation = Aggregation.match(
                Criteria.where("room.joinAccountIds").in(accountId)
        );


        AggregationOperation sortOperation = Aggregation.sort(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        AggregationOperation groupOperation = Aggregation.group("roomId")
                .first("message").as("lastMessage")
                .first("createdAt").as("lastMessageTime")
                .first("roomId").as("roomId")
                .first("room.name").as("roomName")
                .first("room.matingId").as("matingId")
                .first("room.roomType").as("roomType");

        AggregationOperation projectOperation = Aggregation.project()
                .andInclude("roomId")
                .andInclude("roomName")
                .andInclude("lastMessage")
                .andInclude("lastMessageTime")
                .andInclude("matingId")
                .andInclude("roomType");

        return Aggregation.newAggregation(
                lookupOperation, unwindOperation, matchOperation, sortOperation, groupOperation, projectOperation
        );
    }
}
