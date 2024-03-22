package com.fitmate.domain.mating.mate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fitmate.domain.mating.mate.domain.vo.EntryFeeInfo;
import com.fitmate.domain.mating.mate.domain.vo.FitPlace;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MyMateRequestsDto {


    private Long matingId;
    private Long thumbnailFileId;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime mateAt;
    private FitPlace fitPlace;
    private Integer permitPeopleCnt;
    private Integer approvedAccountCnt;
    private Integer entryFee;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestAt;

    @QueryProjection
    public MyMateRequestsDto(Long id, Set<Long> introImages, String title, LocalDateTime mateAt, FitPlace fitPlace, Integer permitPeopleCnt, Integer approvedAccountCnt, EntryFeeInfo entryFeeInfo, LocalDateTime createdAt) {
        this.matingId = id;
        this.thumbnailFileId = getThumbnailFileId(introImages);
        this.title = title;
        this.mateAt = mateAt;
        this.fitPlace = fitPlace;
        this.permitPeopleCnt = permitPeopleCnt;
        this.approvedAccountCnt = approvedAccountCnt;
        this.entryFee = entryFeeInfo.getEntryFee();
        this.requestAt = createdAt;
    }

    private Long getThumbnailFileId(Set<Long> introImageIds) {
        long max = 0L;
        for (Long introImageId : introImageIds) {
            max = Math.max(max, introImageId);
        }
        return max;
    }
}
