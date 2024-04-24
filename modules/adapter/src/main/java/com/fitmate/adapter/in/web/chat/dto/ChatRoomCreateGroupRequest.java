package com.fitmate.adapter.in.web.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Schema(description = "그룹 채팅방 생성 요청 DTO")
public class ChatRoomCreateGroupRequest {
    @NotNull
    @Schema(description = "종속된 메이팅 식별 ID", example = "1")
    private Long mateId;
    @NotNull
    @Schema(description = "생성 요청 회원 ID", example = "1")
    private Long accountId;

    public ChatRoomCreateGroupRequest(Long mateId, Long accountId) {
        this.mateId = mateId;
        this.accountId = accountId;
    }
}
