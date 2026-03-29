package com.fitmate.adapter.out.persistence.mongo.chat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "ChatReadStatus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatReadStatusMongoEntity {

    @MongoId(targetType = FieldType.STRING)
    private String id;

    private String roomId;

    private Long accountId;

    private Date lastReadAt;

    public ChatReadStatusMongoEntity(String roomId, Long accountId, Date lastReadAt) {
        this.id = roomId + "_" + accountId;
        this.roomId = roomId;
        this.accountId = accountId;
        this.lastReadAt = lastReadAt;
    }
}
