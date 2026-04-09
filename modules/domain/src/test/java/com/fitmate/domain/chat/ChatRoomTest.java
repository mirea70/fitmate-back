package com.fitmate.domain.chat;

import com.fitmate.domain.chat.enums.RoomType;
import com.fitmate.domain.chat.room.ChatRoom;
import com.fitmate.domain.chat.room.ChatRoomId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatRoom 도메인 테스트")
class ChatRoomTest {

    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        chatRoom = ChatRoom.withId(
                new ChatRoomId("room1"),
                "테스트방",
                1L,
                new Date(),
                new HashSet<>(Set.of(1L, 2L)),
                RoomType.GROUP,
                1
        );
    }

    @Test
    @DisplayName("참여자 추가")
    void addJoinAccountId() {
        chatRoom.addJoinAccountId(3L);
        assertThat(chatRoom.getJoinAccountIds()).contains(3L);
    }

    @Test
    @DisplayName("null 참여자 추가 시 무시")
    void addNullJoinAccountId() {
        int before = chatRoom.getJoinAccountIds().size();
        chatRoom.addJoinAccountId(null);
        assertThat(chatRoom.getJoinAccountIds()).hasSize(before);
    }

    @Test
    @DisplayName("참여자 제거")
    void removeJoinAccountId() {
        chatRoom.removeJoinAccountId(2L);
        assertThat(chatRoom.getJoinAccountIds()).doesNotContain(2L);
    }

    @Test
    @DisplayName("null 참여자 제거 시 무시")
    void removeNullJoinAccountId() {
        int before = chatRoom.getJoinAccountIds().size();
        chatRoom.removeJoinAccountId(null);
        assertThat(chatRoom.getJoinAccountIds()).hasSize(before);
    }

    @Test
    @DisplayName("방 이름 업데이트")
    void updateRoomName() {
        chatRoom.updateRoomName("새로운방");
        assertThat(chatRoom.getName()).isEqualTo("새로운방");
    }

    @Test
    @DisplayName("방 이름이 null이면 변경 안됨")
    void updateRoomNameNull() {
        chatRoom.updateRoomName(null);
        assertThat(chatRoom.getName()).isEqualTo("테스트방");
    }

    @Test
    @DisplayName("방 이름이 빈 문자열이면 변경 안됨")
    void updateRoomNameEmpty() {
        chatRoom.updateRoomName("");
        assertThat(chatRoom.getName()).isEqualTo("테스트방");
    }

    @Test
    @DisplayName("createByName — DM 타입으로 생성")
    void createByName() {
        ChatRoom dm = ChatRoom.createByName("DM방");
        assertThat(dm.getName()).isEqualTo("DM방");
        assertThat(dm.getRoomType()).isEqualTo(RoomType.DM);
        assertThat(dm.getId()).isNull();
    }

    @Test
    @DisplayName("joinAccountIds가 null인 방에 참여자 추가")
    void addJoinAccountIdWhenNull() {
        ChatRoom room = ChatRoom.withoutId("방", 1L, new Date(), null, RoomType.GROUP);
        room.addJoinAccountId(5L);
        assertThat(room.getJoinAccountIds()).contains(5L);
    }
}
