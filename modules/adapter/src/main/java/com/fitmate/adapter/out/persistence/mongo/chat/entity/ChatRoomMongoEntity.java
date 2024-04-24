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
import java.util.Set;

@Document(collection = "ChatRoom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomMongoEntity {

    @MongoId(targetType = FieldType.STRING)
    private String id;

    @Column(nullable = false)
    private String name;
    @Column
    private Long mateId;
    @CreatedDate
    private Date createdAt;
    @Column
    private Set<Long> joinAccountIds;

    @Version
    private Integer version;
    @Column(nullable = false)
    private String roomType;

    public ChatRoomMongoEntity(String id, String name, Long mateId, Date createdAt, Set<Long> joinAccountIds, Integer version, String roomType) {
        this.id = id;
        this.name = name;
        this.mateId = mateId;
        this.createdAt = createdAt;
        this.joinAccountIds = joinAccountIds;
        this.version = version;
        this.roomType = roomType;
    }
}
