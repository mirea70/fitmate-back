package com.fitmate.app.mate.mating.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fitmate.app.mate.mating.vo.EntryFeeDataInfo;
import com.fitmate.domain.mating.mate.domain.enums.FitCategory;
import com.fitmate.domain.mating.mate.domain.enums.GatherType;
import com.fitmate.domain.mating.mate.domain.enums.PermitGender;
import com.fitmate.domain.mating.mate.domain.vo.EntryFeeInfo;
import com.fitmate.domain.mating.mate.domain.vo.FitPlace;
import com.fitmate.domain.mating.mate.domain.vo.PermitAges;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class MatingDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "메이팅 글 응답 DTO")
    public static class Response {

        @Schema(description = "메이팅 글 식별 ID", example = "1")
        private Long id;

        @Schema(description = "운동 종류", example = "FITNESS")
        private FitCategory fitCategory;

        @Schema(description = "메이팅 글 제목", example = "운동 메이트 구합니다.")
        private String title;

        @Schema(description = "메이팅 소개글", example = "같이 운동하실 분")
        private String introduction;

        @Schema(description = "메이팅 소개 이미지 식별 ID(다수)", example = "[1,2,3]")
        private List<Long> introImages;

        @Schema(description = "운동 일자", example = "2024-03-11'T'14:00:00")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime mateAt;

        @Schema(description = "운동 장소")
        private FitPlace fitPlace;

        @Schema(description = "모집 종류", example = "AGREE")
        private GatherType gatherType;

        @Schema(description = "허용 성별", example = "ALL")
        private PermitGender permitGender;

        @Schema(description = "허용 나이대")
        private PermitAges permitAges;

        @Schema(description = "허용 인원", example = "5")
        private Integer permitPeopleCnt;

        @Schema(description = "작성자 닉네임", example = "자두")
        private String writerNickName;

        @Schema(description = "참여비 유무 여부", example = "false")
        private boolean hasEntryFee;

        @Schema(description = "참여비 세부정보")
        private EntryFeeDataInfo entryFeeInfo;

        @Schema(description = "메이트 신청 대기자 식별 ID 리스트", example = "[1,5,6]")
        private Set<Long> waitingAccountIds;

        @Schema(description = "메이트 신청 승인자 식별 ID 리스트", example = "[3]")
        private Set<Long> approvedAccountIds;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "나의 메이트 글 목록 아이템 DTO")
    public static class MyMateResponse {

        @Schema(description = "메이트 글 ID", example = "1")
        private Long id;
        @Schema(description = "썸네일 이미지 ID (소개 이미지 중 하나)", example = "11")
        private Long thumbnailFileId;
        @Schema(description = "메이팅 글 제목", example = "제목이다")
        private String title;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "메이팅 시간(만나는 시간)", example = "24/03/28 01:45:13.211270000")
        private LocalDateTime mateAt;
        @Schema(description = "메이팅 장소(만나는 장소 : 장소 이름 및 주소)")
        private FitPlace fitPlace;
        @Schema(description = "허용 인원수", example = "20")
        private Integer permitPeopleCnt;
        @Schema(description = "승인된 인원수", example = "1")
        private Integer approvedAccountCnt;
        @Schema(description = "총 참여비", example = "20000")
        private Integer entryFee;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "작성된 시간", example = "24/03/28 01:45:13.211270000")
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "메이팅 글 작성 요청 DTO")
    public static class Create {
        @Schema(description = "운동 종류", example = "FITNESS")
        @NotNull(message = "카테고리 입력은 필수입니다.")
        private FitCategory fitCategory;

        @Schema(description = "메이팅 글 제목", example = "운동 메이트 구합니다.")
        @NotNull(message = "제목 입력은 필수입니다.")
        private String title;

        @Schema(description = "메이팅 소개글", example = "같이 운동하실 분")
        private String introduction;
        @JsonIgnore
        private List<MultipartFile> introImages;

        @Schema(description = "운동 일자", example = "2024-03-11'T'14:00:00")
        @NotNull(message = "메이팅 일자 입력은 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime mateAt;

        @Schema(description = "운동 장소")
        @NotNull(message = "운동 장소 입력은 필수입니다.")
        private FitPlace fitPlace;

        @Schema(description = "모집 종류", example = "AGREE")
        @NotNull(message = "모집 종류 입력은 필수입니다.")
        private GatherType gatherType;

        @Schema(description = "허용 성별", example = "ALL")
        @NotNull(message = "허용 성별 입력은 필수입니다.")
        private PermitGender permitGender;

        @Schema(description = "허용 나이대")
        @NotNull(message = "허용 나이대 입력은 필수입니다.")
        private PermitAges permitAges;

        @Schema(description = "허용 인원", example = "5")
        @NotNull(message = "허용 인원 입력은 필수입니다.")
        private Integer permitPeopleCnt;

        @JsonIgnore
        private Long writerId;

        @Schema(description = "참여비 유무 여부", example = "false")
        private boolean hasEntryFee;

        @Schema(description = "참여비 세부정보")
        private EntryFeeDataInfo entryFeeInfo;

        @Schema(description = "메이트 신청시 답변받을 질문", example = "어떤 운동 스타일을 좋아하시나요?")
        @NotNull(message = "신청질문 입력은 필수입니다.")
        private String comeQuestion;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "메이팅 글 목록 조회 요청 DTO")
    public static class ListRequest {
        @Schema(description = "최하단의 메이팅 식별 ID 값 : 이후 메이팅 글들이 추가노출됨", example = "10")
        @NotNull(message = "직전 마지막 ID 값은 필수입니다.")
        private Long lastMatingId;
        @Schema(description = "한번에 가져올 메이팅 글의 수", example = "10")
        @NotNull(message = "데이터 최대 크기 값은 필수입니다.")
        private int limit;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "메이트 신청 DTO")
    public static class Apply {

        @JsonIgnore
        private Long matingId;

        @JsonIgnore
        private Long accountId;

        @Schema(description = "메이트 신청 질문에 대한 답변", example = "스쿼트 위주로 해요")
        @NotNull(message = "신청질문 답변 입력은 필수입니다.")
        private String comeAnswer;

        @JsonIgnore
        private MateRequest.ApproveStatus approveStatus;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "메이트 신청 승인 요청 DTO")
    public static class Approve {

        @JsonIgnore
        private Long matingId;

        @Schema(description = "승인할 회원 식별 ID 리스트", example = "[1,2]")
        private Set<Long> accountIds;
    }
}
