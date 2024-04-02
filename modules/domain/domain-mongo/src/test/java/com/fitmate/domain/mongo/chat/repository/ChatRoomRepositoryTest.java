package com.fitmate.domain.mongo.chat.repository;

import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    @DisplayName("채팅방 생성 테스트")
    public void createChatRoomTest() {
        // given
//        String expectId = UUID.randomUUID().toString();
        String expectName = "테스트방1";
        ChatRoom chatRoom = ChatRoom.builder()
//                .id(expectId)
                .name(expectName)
                .matingId(1L)
                .build();

        // when
        ChatRoom savedChatRoom = chatRoomRepository.insert(chatRoom);
        // then
        assertEquals(expectName, savedChatRoom.getName());
        System.out.println("savedChatRoom.getName() = " + savedChatRoom.getName());
        System.out.println("expectName = " + expectName);
//        assertEquals(expectId, savedChatRoom.getId());
    }
    @Test
    @DisplayName("채팅방 조회 테스트")
    public void findChatRoomTest() {
        // given
        String expectId = UUID.randomUUID().toString();
        String expectName = "테스트방2";
        saveBefore(expectId, expectName);
        // when
        ChatRoom findChatRoom = chatRoomRepository.findById(expectId).orElseThrow(
                () -> new RuntimeException("채팅방이 없습니다.")
        );
        // then
        assertEquals(expectName, findChatRoom.getName());
        System.out.println("findChatRoom.getName() = " + findChatRoom.getName());
        System.out.println("expectName = " + expectName);
        assertEquals(expectId, findChatRoom.getId());
    }

    private void saveBefore(String id, String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .build();
        chatRoomRepository.insert(chatRoom);
    }

    @Test
    @DisplayName("채팅방 수정 테스트")
    public void updateChatRoomTest() {
        // given
        String expectId = UUID.randomUUID().toString();
        saveBefore(expectId, "수정전");
        ChatRoom findChatRoom = chatRoomRepository.findById(expectId).orElseThrow(
                () -> new RuntimeException("데이터가 없습니다.")
        );
        // when
        String changeName = "수정방2";
        findChatRoom.updateRoomName(changeName);
        ChatRoom changedChatRoom = chatRoomRepository.save(findChatRoom);
        // then
        assertEquals(expectId, changedChatRoom.getId());
        assertEquals(changeName, changedChatRoom.getName());
        System.out.println("findChatRoom.getName() = " + findChatRoom.getName());
        System.out.println("changedChatRoom.getName() = " + changedChatRoom.getName());
    }

    @Test
    @DisplayName("채팅방 삭제 테스트")
    public void deleteChatRoomTest() {
        // given
        String expectId = UUID.randomUUID().toString();
        saveBefore(expectId, "수정전");
        ChatRoom findChatRoom = chatRoomRepository.findById(expectId).orElseThrow(
                () -> new RuntimeException("데이터가 없습니다.")
        );
        System.out.println("findChatRoom.getId() = " + findChatRoom.getId());
        // when
        chatRoomRepository.delete(findChatRoom);
        // then
        assertFalse(chatRoomRepository.existsById(expectId));
    }
}
