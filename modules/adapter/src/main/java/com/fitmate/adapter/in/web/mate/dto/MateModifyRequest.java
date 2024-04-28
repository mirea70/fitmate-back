package com.fitmate.adapter.in.web.mate.dto;

import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.MateFee;
import com.fitmate.domain.mate.enums.PermitGender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@Schema(description = "메이트 글 수정 요청 DTO")
public class MateModifyRequest {
    @Schema(description = "운동 종류", example = "FITNESS")
    private FitCategory fitCategory;

    @Schema(description = "글 제목", example = "운동 메이트 구합니다.")
    private String title;

    @Schema(description = "소개글", example = "같이 운동하실 분")
    private String introduction;

    @Schema(description = "메이트 소개 이미지 식별 ID 리스트", example = "[1,2,3]")
    private Set<Long> introImageIds;

    @Schema(description = "운동 일자", example = "2024-03-11'T'14:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime mateAt;

    @Schema(description = "운동 장소 이름", example = "험블짐")
    private String fitPlaceName;

    @Schema(description = "운동 장소 주소", example = "서울시 용산구 132-7번지 지하1층")
    private String fitPlaceAddress;

    @Schema(description = "모집 종류", example = "AGREE")
    private GatherType gatherType;

    @Schema(description = "허용 성별", example = "ALL")
    private PermitGender permitGender;

    @Schema(description = "허용 최대 나이")
    @Max(value = 50)
    private Integer permitMaxAge;

    @Schema(description = "허용 최소 나이")
    @Min(value = 15)
    private Integer permitMinAge;

    @Schema(description = "허용 인원", example = "5")
    private Integer permitPeopleCnt;

    @Schema(description = "참여비 리스트", example = "[{\"name\": \"장소 대여비\", \"fee\": 10000}]")
    private List<MateFee> mateFees;

    @Schema(description = "메이트 신청시 답변받을 질문", example = "어떤 운동 스타일을 좋아하시나요?")
    private String applyQuestion;
}
