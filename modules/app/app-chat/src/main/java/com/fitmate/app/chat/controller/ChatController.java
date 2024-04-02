package com.fitmate.app.chat.controller;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.app.chat.dto.ChatRoomDto;
import com.fitmate.app.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoomDto.Response> createGroupChatRoom(@RequestBody ChatRoomDto.CreateGroup createGroup) {
        return ResponseEntity.ok(chatService.createGroupChatRoom(createGroup));
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessagesBtRoomId(roomId));
    }
}
