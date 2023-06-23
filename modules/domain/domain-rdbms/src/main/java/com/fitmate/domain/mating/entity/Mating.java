package com.fitmate.domain.mating.entity;

import com.fitmate.domain.base.BaseDomain;
import com.fitmate.domain.mating.converter.SetConverter;
import com.fitmate.domain.mating.enums.FitCategory;
import com.fitmate.domain.mating.enums.GatherType;
import com.fitmate.domain.mating.enums.PermitGender;
import com.fitmate.domain.mating.vo.EntryFeeInfo;
import com.fitmate.domain.mating.vo.FitPlace;
import com.fitmate.domain.mating.vo.PermitAges;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "id", callSuper = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder(builderMethodName = "innerBuilder")
public class Mating extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mating_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private final FitCategory fitCategory;

    @Column(nullable = false)
    private final String title;

    @Column(columnDefinition = "TEXT")
    private final String introduction;

    @Column
    @Convert(converter = SetConverter.class)
    @Builder.Default
    private Set<Long> introImages = null;

    @Column(nullable = false)
    private final LocalDateTime mateAt;

    @Embedded
    private final FitPlace fitPlace;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private final GatherType gatherType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private final PermitGender permitGender;

    @Embedded
    private final PermitAges permitAges;

    @Column(nullable = false)
    private final Integer permitPeopleCnt;

    @Column(nullable = false)
    @Builder.Default
    private boolean hasEntryFee = false;

    @Embedded
    @Builder.Default
    private EntryFeeInfo entryFeeInfo = null;

    // 작성자 정보 추가 필요

    public static MatingBuilder builder(FitCategory fitCategory, String title, String introduction,
                  LocalDateTime mateAt, FitPlace fitPlace,
                  GatherType gatherType, PermitGender permitGender, PermitAges permitAges,
                  Integer permitPeopleCnt) {

        return innerBuilder().fitCategory(fitCategory).title(title).introduction(introduction)
                .mateAt(mateAt).fitPlace(fitPlace).gatherType(gatherType).permitGender(permitGender)
                .permitAges(permitAges).permitPeopleCnt(permitPeopleCnt);
    }
}
