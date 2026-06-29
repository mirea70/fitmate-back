package com.fitmate.adapter.in.web.mate.dto;

import com.fitmate.adapter.in.web.common.dto.SliceRequest.SortDir;
import com.fitmate.domain.mate.enums.FitCategory;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "메이트 목록 조회 요청 DTO")
public record MateListRequest(
        @Schema(description = "조회 페이지", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer page,

        @Schema(description = "한 번에 조회할 페이지 크기", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer size,

        @Schema(description = "정렬 방향", example = "DESC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        SortDir sortDir,

        @Schema(description = "정렬할 속성", example = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String sortProperty,

        @Schema(description = "검색할 키워드 : 제목, 지역", example = "분당구", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String keyword,

        @Schema(description = "요일 인덱스 (0~6)", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer dayOfWeek,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "필터 시작 날짜", example = "2024-03-11T14:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDateTime startMateAt,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "필터 마지막 날짜", example = "2024-04-11T14:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        LocalDateTime endMateAt,

        @ArraySchema(
                arraySchema = @Schema(description = "필터할 지역 (3개까지 가능)", example = "[\"서울 강남구\"]"),
                schema = @Schema(example = "서울 강남구")
        )
        List<String> fitPlaceRegions,

        @Schema(description = "허용 최대 나이", example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer permitMaxAge,

        @Schema(description = "허용 최소 나이", example = "20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer permitMinAge,

        @Schema(description = "허용 인원수 필터 시작값", example = "20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer startLimitPeopleCnt,

        @Schema(description = "허용 인원수 필터 마지막값", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer endLimitPeopleCnt,

        @Schema(description = "운동 종목", example = "FITNESS", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        FitCategory fitCategory,

        @Schema(description = "마감 포함 여부", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Boolean includeClosed
) {
    public MateListRequest {
        page = page != null ? page : 0;
        size = size != null ? size : 15;
        sortDir = sortDir != null ? sortDir : SortDir.DESC;
        sortProperty = sortProperty != null ? sortProperty : "createdAt";
    }
}
