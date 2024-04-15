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
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "그룹 채팅방 생성", description = "그룹 채팅방 생성 API")
    @PostMapping("/room/group")
    public ResponseEntity<ChatRoomDto.Response> createGroupChatRoom(@Valid @RequestBody ChatRoomDto.CreateGroup createGroup) {
        return ResponseEntity.ok(chatService.createGroupChatRoom(createGroup));
    }

    @Operation(summary = "DM 채팅방 생성", description = "DM 채팅방 생성 API")
    @PostMapping("/room/dm")
    public ResponseEntity<ChatRoomDto.Response> createDmChatRoom(@Valid @RequestBody ChatRoomDto.CreateDM createGroup) {
        return ResponseEntity.ok(chatService.createDmChatRoom(createGroup));
    }

    @Operation(summary = "채팅방 내 메시지 조회", description = "채팅방 내 메시지들 조회 API (정렬 기준 : 생성일 내림차순)")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getMessagesByRoomId(roomId));
    }

    @Operation(summary = "나의 채팅방 목록 조회", description = "나의 채팅방 목록 조회 API")
    @GetMapping("/my/rooms")
    public ResponseEntity<List<ChatRoomDto.Response>> getMyChatRooms(@RequestParam Long accountId) {
        return ResponseEntity.ok(chatService.getMyChatRooms(accountId));
    }
}
