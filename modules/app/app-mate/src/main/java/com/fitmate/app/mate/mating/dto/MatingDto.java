package com.fitmate.app.mate.mating.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fitmate.app.mate.mating.vo.EntryFeeDataInfo;
import com.fitmate.domain.mating.domain.enums.FitCategory;
import com.fitmate.domain.mating.domain.enums.GatherType;
import com.fitmate.domain.mating.domain.enums.PermitGender;
import com.fitmate.domain.mating.domain.vo.FitPlace;
import com.fitmate.domain.mating.domain.vo.PermitAges;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class MatingDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;

        private FitCategory fitCategory;

        private String title;

        private String introduction;

        private List<Long> introImages;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime mateAt;

        private FitPlace fitPlace;

        private GatherType gatherType;

        private PermitGender permitGender;

        private PermitAges permitAges;

        private Integer permitPeopleCnt;

        private String writerNickName;

        private boolean hasEntryFee;

        private EntryFeeDataInfo entryFeeInfo;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        @NotNull(message = "카테고리 입력은 필수입니다.")
        private FitCategory fitCategory;
        @NotNull(message = "제목 입력은 필수입니다.")
        private String title;
        private String introduction;
        @JsonIgnore
        private List<MultipartFile> introImages;
        @NotNull(message = "메이팅 일자 입력은 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime mateAt;
        @NotNull(message = "운동 장소 입력은 필수입니다.")
        private FitPlace fitPlace;
        @NotNull(message = "모집 종류 입력은 필수입니다.")
        private GatherType gatherType;
        @NotNull(message = "허용 성별 입력은 필수입니다.")
        private PermitGender permitGender;
        @NotNull(message = "허용 나이대 입력은 필수입니다.")
        private PermitAges permitAges;
        @NotNull(message = "허용 인원 입력은 필수입니다.")
        private Integer permitPeopleCnt;
        @NotNull(message = "작성자 ID 입력은 필수입니다.")
        private Long writerId;
        private boolean hasEntryFee;
        private EntryFeeDataInfo entryFeeInfo;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListRequest {
        @NotNull(message = "직전 마지막 ID 값은 필수입니다.")
        private Long lastMatingId;
        @NotNull(message = "데이터 최대 크기 값은 필수입니다.")
        private int limit;
    }
}
