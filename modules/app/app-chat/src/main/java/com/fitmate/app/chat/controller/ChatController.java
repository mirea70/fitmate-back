package com.fitmate.app.chat.controller;

import com.fitmate.app.chat.dto.ChatMessageDto;
import com.fitmate.app.chat.dto.ChatRoomDto;
import com.fitmate.app.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Chat", description = "채팅 관리 API")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 생성", description = """
            채팅방 생성 API
            
            **roomType = GROUP** : matingId가 없을 시 400 Bad Request 에러가 발생, accountId는 필요 X
            
            **roomType = DM** : accountId가 없을 시 400 Bad Request 에러가 발생, matingId는 필요 X
            """)
    @PostMapping("/room")
    public ResponseEntity<ChatRoomDto.Response> createGroupChatRoom(@Valid @RequestBody ChatRoomDto.Create createGroup) {
        return ResponseEntity.ok(chatService.createChatRoom(createGroup));
    }

    @Operation(summary = "채팅방 내 메시지 조회", description = "채팅방 내 메시지 조회 API")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessagesBtRoomId(roomId));
    }
}
