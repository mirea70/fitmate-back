package com.fitmate.app.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


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
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "그룹 채팅방 생성 요청 DTO")
    public static class CreateGroup {
        @Schema(description = "종속된 메이팅 식별 ID", example = "1")
        private Long matingId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateDm {
        private Long accountId;
    }
}
