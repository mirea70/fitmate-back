package com.fitmate.domain.chat.room;

import com.fitmate.domain.chat.enums.RoomType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {

    private ChatRoomId id;

    private String name;

    private Long mateId;

    private Date createdAt;

    private Set<Long> joinAccountIds;

    private RoomType roomType;

    private Integer version;

    public static ChatRoom withId(ChatRoomId id, String name, Long mateId, Date createdAt, Set<Long> joinAccountIds, RoomType roomType, Integer version) {
        return new ChatRoom(
                id,
                name,
                mateId,
                createdAt,
                joinAccountIds,
                roomType,
                version
        );
    }

    public static ChatRoom withoutId(String name, Long mateId, Date createdAt, Set<Long> joinAccountIds, RoomType roomType) {
        return new ChatRoom(
                null,
                name,
                mateId,
                createdAt,
                joinAccountIds,
                roomType,
                null
        );
    }

    public static ChatRoom createByName(String name) {
        return new ChatRoom(
                null,
                name,
                null,
                null,
                null,
                RoomType.DM,
                null
        );
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
}
