package com.fitmate.domain.mating.dto;

import com.fitmate.domain.mating.domain.enums.FitCategory;
import com.fitmate.domain.mating.domain.enums.GatherType;
import com.fitmate.domain.mating.domain.enums.PermitGender;
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
    private LocalDateTime mateAt;

    private String fitPlaceName;

    private String fitPlaceAddress;

    private GatherType gatherType;

    private PermitGender permitGender;

    private Integer permitMaxAge;

    private Integer permitMinAge;

    private Integer permitPeopleCnt;

    @QueryProjection
    public MatingReadResponseDto(Long id, FitCategory fitCategory, String title,
                                 LocalDateTime mateAt, String fitPlaceName,
                                 String fitPlaceAddress, GatherType gatherType,
                                 PermitGender permitGender, Integer permitMaxAge,
                                 Integer permitMinAge, Integer permitPeopleCnt) {
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
    }
}
