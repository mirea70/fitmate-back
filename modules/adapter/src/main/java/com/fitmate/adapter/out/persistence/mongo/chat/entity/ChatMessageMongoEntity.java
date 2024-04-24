package com.fitmate.adapter.out.persistence.mongo.chat.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Column;
import java.util.Date;

@Document(collection = "ChatMessage")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessageMongoEntity {

    @MongoId(targetType = FieldType.STRING)
    private String id;
    @Column(nullable = false)
    private String roomId;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private Long senderId;
    @Column(nullable = false)
    private String senderNickName;
    @CreatedDate
    private Date createdAt;
    @Version
    private Integer version;

    public ChatMessageMongoEntity(String id, String roomId, String message, Long senderId, String senderNickName, Date createdAt, Integer version) {
        this.id = id;
        this.roomId = roomId;
        this.message = message;
        this.senderId = senderId;
        this.senderNickName = senderNickName;
        this.createdAt = createdAt;
        this.version = version;
    }
}
