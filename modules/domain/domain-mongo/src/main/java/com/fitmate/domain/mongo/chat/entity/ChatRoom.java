package com.fitmate.domain.mongo.chat.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Set<Long> joinAccountIds;

    @Version
    private Integer version;

    @Field(targetType = FieldType.STRING)
    private RoomType roomType;

    @Builder
    public ChatRoom(String name, Long matingId) {
        this.name = name;
        if(matingId != null) {
            this.matingId = matingId;
            this.roomType = RoomType.GROUP;
        }
        else this.roomType = RoomType.DM;
    }

    public void updateRoomType(String name) {
        this.name = name;
        this.roomType = RoomType.DM;
    }

    public void updateRoomName(String name) {
        if(name == null || name.isEmpty()) return;
        this.name = name;
    }

    public void addJoinAccountId(Long accountId) {
        if(accountId == null) return;
        if(this.joinAccountIds == null) this.joinAccountIds = new HashSet<>();

        this.joinAccountIds.add(accountId);
    }

    public void removeJoinAccountId(Long accountId) {
        if(accountId == null) return;
        if(this.joinAccountIds == null || this.joinAccountIds.isEmpty()) return;

        this.joinAccountIds.remove(accountId);
    }

    public enum RoomType {
        GROUP("그룹"),
        DM("개인");
        public final String label;
        RoomType(String label) { this.label = label; }
    }
}
