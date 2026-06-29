package com.fitmate.adapter.in.web.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "DM채팅방 생성 요청 DTO")
public record ChatRoomCreateDmRequest(
        @NotNull
        @Schema(description = "발신 요청 회원 ID", example = "1")
        Long fromAccountId,

        @NotNull
        @Schema(description = "수신 회원 ID", example = "1")
        Long toAccountId
) {
}
