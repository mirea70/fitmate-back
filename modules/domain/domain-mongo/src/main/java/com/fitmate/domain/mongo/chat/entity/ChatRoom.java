package com.fitmate.domain.mongo.chat.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document
@Getter
@NoArgsConstructor
public class ChatRoom {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    private String name;

    private Long matingId;

    @CreatedDate
    private Date createdAt;

    @Version
    private Integer version;

    @Builder
    public ChatRoom(String name, Long matingId) {
        this.name = name;
        this.matingId = matingId;
    }

    @Builder
    public ChatRoom(String name) {
        this.name = name;
    }

    public void updateRoomName(String name) {
        if(name == null || name.isEmpty()) return;
        this.name = name;
    }
}
