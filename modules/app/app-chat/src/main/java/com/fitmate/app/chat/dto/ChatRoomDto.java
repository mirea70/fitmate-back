package com.fitmate.app.chat.dto;

import com.fitmate.domain.mongo.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;


public class ChatRoomDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 데이터 응답 DTO")
    public static class Response {
        @Schema(description = "채팅방 식별 ID", example = "660d293a82abe518401b5111")
        private String id;
        @Schema(description = "채팅방 제목", example = "내일 만나는날")
        private String name;
        @Schema(description = "종속된 메이팅 식별 ID", example = "1")
        private Long matingId;
        @Schema(description = "채팅방 종류", example = "GROUP")
        private ChatRoom.RoomType roomType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "그룹채팅방 생성 요청 DTO")
    public static class CreateGroup {
        @NotNull
        @Schema(description = "종속된 메이팅 식별 ID", example = "1")
        private Long matingId;
        @NotNull
        @Schema(description = "생성 요청 회원 ID", example = "1")
        private Long accountId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "DM채팅방 생성 요청 DTO")
    public static class CreateDM {
        @NotNull
        @Schema(description = "발신 요청 회원 ID", example = "1")
        private Long fromAccountId;
        @NotNull
        @Schema(description = "수신 회원 ID", example = "1")
        private Long toAccountId;
    }
}
