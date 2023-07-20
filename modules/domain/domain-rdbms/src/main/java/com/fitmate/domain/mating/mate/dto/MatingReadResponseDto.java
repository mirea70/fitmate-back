package com.fitmate.domain.mating.mate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fitmate.domain.mating.mate.domain.enums.FitCategory;
import com.fitmate.domain.mating.mate.domain.enums.GatherType;
import com.fitmate.domain.mating.mate.domain.enums.PermitGender;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class MatingReadResponseDto {

    private Long id;

    private FitCategory fitCategory;

    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime mateAt;

    private String fitPlaceName;

    private String fitPlaceAddress;

    private GatherType gatherType;

    private PermitGender permitGender;

    private Integer permitMaxAge;

    private Integer permitMinAge;

    private Integer permitPeopleCnt;

    private int waitingAccountCnt;

    private int approvedAccountCnt;

    @QueryProjection
    public MatingReadResponseDto(Long id, FitCategory fitCategory, String title,
                                 LocalDateTime mateAt, String fitPlaceName,
                                 String fitPlaceAddress, GatherType gatherType,
                                 PermitGender permitGender, Integer permitMaxAge,
                                 Integer permitMinAge, Integer permitPeopleCnt,
                                 int waitingAccountCnt, int approvedAccountCnt) {
        this.id = id;
        this.fitCategory = fitCategory;
        this.title = title;
        this.mateAt = mateAt;
        this.fitPlaceName = fitPlaceName;
        this.fitPlaceAddress = fitPlaceAddress;
        this.gatherType = gatherType;
        this.permitGender = permitGender;
        this.permitMaxAge = permitMaxAge;
        this.permitMinAge = permitMinAge;
        this.permitPeopleCnt = permitPeopleCnt;
        this.waitingAccountCnt = waitingAccountCnt;
        this.approvedAccountCnt = approvedAccountCnt;
    }
}
