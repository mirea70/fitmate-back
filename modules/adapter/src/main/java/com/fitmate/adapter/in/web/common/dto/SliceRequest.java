package com.fitmate.adapter.in.web.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SliceRequest {
    @Schema(description = "조회 페이지", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer page = 0;
    @Schema(description = "한 번에 조회할 페이지 크기", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer size = 15;
    @Schema(description = "정렬할 속성", example = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortProperty = "createdAt";
    @Schema(description = "정렬 방향", example = "DESC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private SortDir sortDir = SortDir.DESC;

    public SliceRequest(Integer page, Integer size, SortDir sortDir, String sortProperty) {
        this.page = page != null ? page : this.page;
        this.size = size != null ? size : this.size;
        this.sortDir = sortDir != null ? sortDir : this.sortDir;
        this.sortProperty = sortProperty != null ? sortProperty : this.sortProperty;
    }

    public enum SortDir {
        ASC,
        DESC,
    }
}
