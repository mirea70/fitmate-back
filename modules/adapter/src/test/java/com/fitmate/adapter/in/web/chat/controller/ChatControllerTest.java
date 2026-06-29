package com.fitmate.adapter.in.web.chat.controller;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.in.web.chat.dto.ChatRoomCreateDmRequest;
import com.fitmate.adapter.in.web.chat.dto.ChatRoomCreateGroupRequest;
import com.fitmate.adapter.in.web.chat.mapper.ChatWebAdapterMapper;
import com.fitmate.port.in.chat.usecase.ChatUseCasePort;
import com.fitmate.port.out.chat.dto.ChatRoomSimpleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("ChatController 테스트")
class ChatControllerTest extends BaseControllerTest {

    @MockBean
    private ChatUseCasePort chatUseCasePort;

    @MockBean
    private ChatWebAdapterMapper chatWebAdapterMapper;

    @Nested
    @DisplayName("POST /api/chat/room/group")
    class CreateGroupChatRoom {

        @Test
        @DisplayName("그룹 채팅방 생성 — 201 Created + Location 헤더")
        void createGroupChatRoomSuccess() throws Exception {
            ChatRoomCreateGroupRequest request = new ChatRoomCreateGroupRequest(1L, 1L);
            given(chatUseCasePort.createGroupChatRoom(any())).willReturn(new ChatRoomSimpleResponse("room-1"));

            mockMvc.perform(post("/api/chat/room/group")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/api/chat/rooms/room-1"))
                    .andExpect(jsonPath("$.roomId").value("room-1"));
        }
    }

    @Nested
    @DisplayName("POST /api/chat/room/dm")
    class CreateDmChatRoom {

        @Test
        @DisplayName("DM 채팅방 생성 — 201 Created + Location 헤더")
        void createDmChatRoomSuccess() throws Exception {
            String requestBody = """
                    {
                        "fromAccountId": 1,
                        "toAccountId": 2
                    }
                    """;
            given(chatUseCasePort.createDmChatRoom(any())).willReturn(new ChatRoomSimpleResponse("dm-1"));

            mockMvc.perform(post("/api/chat/room/dm")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/api/chat/rooms/dm-1"))
                    .andExpect(jsonPath("$.roomId").value("dm-1"));
        }
    }
}
