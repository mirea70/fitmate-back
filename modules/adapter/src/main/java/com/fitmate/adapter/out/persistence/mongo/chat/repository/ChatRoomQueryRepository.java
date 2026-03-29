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
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatRoomQueryRepository {
    private final MongoTemplate mongoTemplate;

    public Optional<String> findRoomIdByExactJoinAccountIds(Set<Long> accountIds) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("joinAccountIds").all(accountIds),
                Criteria.where("joinAccountIds").size(accountIds.size())
        ));
        ChatRoomMongoEntity entity = mongoTemplate.findOne(query, ChatRoomMongoEntity.class);
        return Optional.ofNullable(entity).map(ChatRoomMongoEntity::getId);
    }

    public List<ChatRoomListItemResponse> findAllByMyAccountId(Long accountId) {
        Aggregation aggregation = getAggregationForFindAllByMyAccountId(accountId);

        AggregationResults<ChatRoomListItemResponse> results = mongoTemplate.aggregate(aggregation, "ChatMessage", ChatRoomListItemResponse.class);
        return results.getMappedResults();
    }

    private Aggregation getAggregationForFindAllByMyAccountId(Long accountId) {
        String readStatusId = "\"_id\": {\"$concat\": [\"$roomId\", \"_\", \"" + accountId + "\"]}";

        AggregationOperation lookupOperation = Aggregation.lookup(
                "ChatRoom", "roomId", "_id", "room"
        );

        AggregationOperation unwindOperation = Aggregation.unwind("room");

        AggregationOperation matchOperation = Aggregation.match(
                Criteria.where("room.joinAccountIds").in(accountId)
        );

        AggregationOperation lookupReadStatus = context -> {
            return new org.bson.Document("$lookup", new org.bson.Document()
                    .append("from", "ChatReadStatus")
                    .append("let", new org.bson.Document("rid", "$roomId"))
                    .append("pipeline", java.util.Arrays.asList(
                            new org.bson.Document("$match", new org.bson.Document("$expr",
                                    new org.bson.Document("$eq", java.util.Arrays.asList(
                                            "$_id",
                                            new org.bson.Document("$concat", java.util.Arrays.asList("$$rid", "_", String.valueOf(accountId)))
                                    ))
                            ))
                    ))
                    .append("as", "readStatus")
            );
        };

        AggregationOperation addReadStatusField = context -> {
            return new org.bson.Document("$addFields", new org.bson.Document()
                    .append("lastReadAt", new org.bson.Document("$ifNull",
                            java.util.Arrays.asList(
                                    new org.bson.Document("$arrayElemAt", java.util.Arrays.asList("$readStatus.lastReadAt", 0)),
                                    new java.util.Date(0)
                            )
                    ))
                    .append("isUnread", new org.bson.Document("$cond", java.util.Arrays.asList(
                            new org.bson.Document("$gt", java.util.Arrays.asList(
                                    "$createdAt",
                                    new org.bson.Document("$ifNull", java.util.Arrays.asList(
                                            new org.bson.Document("$arrayElemAt", java.util.Arrays.asList("$readStatus.lastReadAt", 0)),
                                            new java.util.Date(0)
                                    ))
                            )),
                            1, 0
                    )))
            );
        };

        AggregationOperation sortOperation = Aggregation.sort(
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        AggregationOperation groupOperation = Aggregation.group("roomId")
                .first("message").as("lastMessage")
                .first("createdAt").as("lastMessageTime")
                .first("roomId").as("roomId")
                .first("room.name").as("roomName")
                .first("room.matingId").as("matingId")
                .first("room.roomType").as("roomType")
                .first("room.joinAccountIds").as("memberAccountIds")
                .sum("isUnread").as("unreadCount");

        AggregationOperation projectOperation = Aggregation.project()
                .andInclude("roomId")
                .andInclude("roomName")
                .andInclude("lastMessage")
                .andInclude("lastMessageTime")
                .andInclude("matingId")
                .andInclude("roomType")
                .andInclude("memberAccountIds")
                .andInclude("unreadCount");

        AggregationOperation finalSortOperation = Aggregation.sort(
                Sort.by(Sort.Direction.DESC, "lastMessageTime")
        );

        return Aggregation.newAggregation(
                lookupOperation, unwindOperation, matchOperation, lookupReadStatus, addReadStatusField, sortOperation, groupOperation, projectOperation, finalSortOperation
        );
    }
}
