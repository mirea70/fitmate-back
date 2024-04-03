package com.fitmate.app.chat.controller;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.app.chat.dto.ChatRoomDto;
import com.fitmate.app.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "회원 관리 API")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "그룹 채팅방 생성", description = "그룹 채팅방 생성 API")
    @PostMapping("/room")
    public ResponseEntity<ChatRoomDto.Response> createGroupChatRoom(@RequestBody ChatRoomDto.CreateGroup createGroup) {
        return ResponseEntity.ok(chatService.createGroupChatRoom(createGroup));
    }

    @Operation(summary = "채팅방 내 메시지 조회", description = "채팅방 내 메시지 조회 API")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessagesBtRoomId(roomId));
    }
}
