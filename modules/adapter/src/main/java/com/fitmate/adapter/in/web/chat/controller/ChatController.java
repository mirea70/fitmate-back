package com.fitmate.adapter.in.web.chat.controller;

import com.fitmate.adapter.WebAdapter;
import com.fitmate.adapter.in.web.chat.dto.ChatRoomCreateDmRequest;
import com.fitmate.adapter.in.web.chat.dto.ChatRoomCreateGroupRequest;
import com.fitmate.adapter.in.web.chat.mapper.ChatWebAdapterMapper;
import com.fitmate.port.in.chat.usecase.ChatUseCasePort;
import com.fitmate.port.out.chat.dto.ChatMessageResponse;
import com.fitmate.port.out.chat.dto.ChatRoomListItemResponse;
import com.fitmate.port.out.chat.dto.ChatRoomSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "03. Chat", description = "채팅 관리 API")
public class ChatController {
    private final ChatUseCasePort chatUseCasePort;
    private final ChatWebAdapterMapper chatWebAdapterMapper;

    @Operation(summary = "그룹 채팅방 생성", description = "그룹 채팅방 생성 API")
    @PostMapping("/room/group")
    public ResponseEntity<ChatRoomSimpleResponse> createGroupChatRoom(@Valid @RequestBody ChatRoomCreateGroupRequest request) {
        return ResponseEntity.ok(chatUseCasePort.createGroupChatRoom(chatWebAdapterMapper.requestToCommand(request)));
    }

    @Operation(summary = "DM 채팅방 생성", description = "DM 채팅방 생성 API")
    @PostMapping("/room/dm")
    public ResponseEntity<ChatRoomSimpleResponse> createDmChatRoom(@Valid @RequestBody ChatRoomCreateDmRequest request) {
        return ResponseEntity.ok(chatUseCasePort.createDmChatRoom(chatWebAdapterMapper.requestToCommand(request)));
    }

    @Operation(summary = "채팅방 내 메시지 조회", description = "채팅방 내 메시지들 조회 API (정렬 기준 : 생성일 내림차순)")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(chatUseCasePort.getMessages(roomId));
    }

    @Operation(summary = "나의 채팅방 목록 조회", description = """
            나의 채팅방 목록 조회 API
            
            **[참고]** 채팅이 1회이상 이루어진 채팅방만 조회됩니다. 채팅 기능 사용방법은 GitHub 저장소를 확인해주세요.
            """)
    @GetMapping("/my/rooms")
    public ResponseEntity<List<ChatRoomListItemResponse>> getMyChatRooms(@RequestParam Long accountId) {
        return ResponseEntity.ok(chatUseCasePort.getMyChatRooms(accountId));
    }
}
