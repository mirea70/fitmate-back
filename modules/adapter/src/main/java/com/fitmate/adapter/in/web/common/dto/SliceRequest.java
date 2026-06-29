package com.fitmate.adapter.in.web.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SliceRequest(
        @Schema(description = "조회 페이지", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer page,

        @Schema(description = "한 번에 조회할 페이지 크기", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer size,

        @Schema(description = "정렬 방향", example = "DESC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        SortDir sortDir,

        @Schema(description = "정렬할 속성", example = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String sortProperty
) {
    public SliceRequest {
        page = page != null ? page : 0;
        size = size != null ? size : 15;
        sortDir = sortDir != null ? sortDir : SortDir.DESC;
        sortProperty = sortProperty != null ? sortProperty : "createdAt";
    }

    public enum SortDir {
        ASC,
        DESC,
    }
}
