package com.fitmate.domain.mating.entity;

import com.fitmate.domain.base.BaseDomain;
import com.fitmate.domain.mating.enums.FitCategory;
import com.fitmate.domain.mating.enums.GatherType;
import com.fitmate.domain.mating.enums.PermitGender;
import com.fitmate.domain.mating.vo.EntryFeeInfo;
import com.fitmate.domain.mating.vo.FitPlace;
import com.fitmate.domain.mating.vo.PermitAges;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id", callSuper = false)
@Getter
public class Mating extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mating_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitCategory fitCategory;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    // 소개 이미지 리스트 (추가 필요)

    @Column(nullable = false)
    private LocalDateTime mateAt;

    @Embedded
    private FitPlace fitPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GatherType gatherType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PermitGender permitGender;

    @Embedded
    private PermitAges permitAges;

    @Column(nullable = false)
    private Integer permitPeopleCnt;

    @Column(nullable = false)
    private boolean isEntryFee = false;

    @Embedded
    private EntryFeeInfo entryFeeInfo;

    // 작성자 정보 추가 필요
}
