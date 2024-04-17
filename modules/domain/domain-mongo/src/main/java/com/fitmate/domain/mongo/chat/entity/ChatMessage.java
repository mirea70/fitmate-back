package com.fitmate.domain.mongo.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ChatMessage {

    @MongoId(targetType = FieldType.STRING)
    private String id;

    private String roomId;

    private String message;

    private Long senderId;

    private String senderNickName;

    @CreatedDate
    private Date createdAt;

    @Version
    private Integer version;
}
