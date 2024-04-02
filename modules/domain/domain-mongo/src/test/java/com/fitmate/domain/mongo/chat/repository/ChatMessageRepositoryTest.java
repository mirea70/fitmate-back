package com.fitmate.domain.mongo.chat.repository;

import com.fitmate.domain.mongo.chat.entity.ChatMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatMessageRepositoryTest {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Test
    @DisplayName("채팅 메시지 삽입 테스트")
    public void createChatMessageTest() {
        // given
//        String expectId = "1";
        String expectMessage = "테스트 메시지1";
        ChatMessage chatMessage = ChatMessage.builder()
//                .id(expectId)
                .roomId("660b9f8550324c6a7d0b1db9")
                .messageType(ChatMessage.MessageType.ENTER)
                .senderNickName("사용자1")
                .message(expectMessage)
                .build();
        // when
        ChatMessage savedChatMessage = chatMessageRepository.insert(chatMessage);
        // then
        assertEquals(expectMessage, savedChatMessage.getMessage());
        System.out.println("expectMessage = " + expectMessage);
        System.out.println("savedChatMessage.getMessage() = " + savedChatMessage.getMessage());
//        assertEquals(expectId, savedChatMessage.getId());
    }
    @Test
    @DisplayName("채팅 메시지 조회 테스트")
    public void findChatMessageTest() {
        // given
        String expectId = "2";
        String expectMessage = "테스트 메시지2";
        ChatMessage chatMessage = ChatMessage.builder()
                .id(expectId)
                .roomId("c16b725a-c2a1-40ca-a9b0-f5d0f16a5c08")
                .messageType(ChatMessage.MessageType.ENTER)
                .senderNickName("사용자2")
                .message(expectMessage)
                .build();
        chatMessageRepository.save(chatMessage);
        // when
        ChatMessage findChatMessage = chatMessageRepository.findById(expectId).orElseThrow();
        // then
        assertEquals(expectMessage, findChatMessage.getMessage());
        System.out.println("expectMessage = " + expectMessage);
        System.out.println("findChatMessage.getMessage() = " + findChatMessage.getMessage());
        assertEquals(expectId, findChatMessage.getId());
    }

    @Test
    @DisplayName("채팅 메시지 삭제 테스트")
    public void deleteChatMessageTest() {
        // given
        String expectId = "1";
        ChatMessage findChatMessage = chatMessageRepository.findById(expectId).orElseThrow();
        System.out.println("findChatMessage.getId() = " + findChatMessage.getId());
        // when
        chatMessageRepository.delete(findChatMessage);
        // then
        assertFalse(chatMessageRepository.existsById(expectId));
    }

}
