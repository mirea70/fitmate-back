package com.fitmate.adapter.in.web.mate.dto;

import com.fitmate.adapter.in.web.common.dto.SliceRequest;
import com.fitmate.domain.mate.enums.FitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "메이트 목록 조회 요청 DTO")
public class MateListRequest extends SliceRequest {
    @Schema(description = "검색할 키워드 : 지역, 카테고리", example = "분당구", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String keyword;

    @Schema(description = "요일 인덱스 (0~6)", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer dayOfWeek;

    @Schema(description = "필터 시작 날짜", example = "2024-03-11'T'14:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startMateAt;

    @Schema(description = "필터 마지막 날짜", example = "2024-04-11'T'14:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endMateAt;

    @Schema(description = "필터할 지역 (3개까지 가능)", example = "[\"서울 강남구\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> fitPlaceRegions;

    @Schema(description = "허용 최대 나이", example = "50", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer permitMaxAge;

    @Schema(description = "허용 최소 나이", example = "20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer permitMinAge;

    @Schema(description = "허용 인원수 필터 시작값", example = "20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer startLimitPeopleCnt;

    @Schema(description = "허용 인원수 필터 마지막값", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer endLimitPeopleCnt;

    @Schema(description = "운동 종목", example = "FITNESS", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private FitCategory fitCategory;

    public MateListRequest(Integer page, Integer size, SortDir sortDir, String sortProperty, String keyword, Integer dayOfWeek, LocalDateTime startMateAt, LocalDateTime endMateAt, List<String> fitPlaceRegions, Integer permitMaxAge, Integer permitMinAge, Integer startLimitPeopleCnt, Integer endLimitPeopleCnt, FitCategory fitCategory) {
        super(page, size, sortDir, sortProperty);
        this.keyword = keyword;
        this.dayOfWeek = dayOfWeek;
        this.startMateAt = startMateAt;
        this.endMateAt = endMateAt;
        this.fitPlaceRegions = fitPlaceRegions;
        this.permitMaxAge = permitMaxAge;
        this.permitMinAge = permitMinAge;
        this.startLimitPeopleCnt = startLimitPeopleCnt;
        this.endLimitPeopleCnt = endLimitPeopleCnt;
        this.fitCategory = fitCategory;
    }
}
