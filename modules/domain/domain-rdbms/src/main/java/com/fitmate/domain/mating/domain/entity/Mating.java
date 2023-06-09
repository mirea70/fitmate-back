package com.fitmate.domain.mating.domain.entity;

import com.fitmate.domain.base.BaseDomain;
import com.fitmate.domain.mating.converter.SetConverter;
import com.fitmate.domain.mating.domain.enums.FitCategory;
import com.fitmate.domain.mating.domain.enums.GatherType;
import com.fitmate.domain.mating.domain.enums.PermitGender;
import com.fitmate.domain.mating.domain.vo.EntryFeeInfo;
import com.fitmate.domain.mating.domain.vo.FitPlace;
import com.fitmate.domain.mating.domain.vo.PermitAges;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "id", callSuper = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(builderMethodName = "innerBuilder")
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

    @Column
    @Convert(converter = SetConverter.class)
    @Builder.Default
    private Set<Long> introImages = null;

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
    private Long writerId;

    @Column(nullable = false)
    @Builder.Default
    private boolean hasEntryFee = false;

    @Embedded
    @Builder.Default
    private EntryFeeInfo entryFeeInfo = null;

    public static MatingBuilder builder(FitCategory fitCategory, String title, String introduction,
                  LocalDateTime mateAt, FitPlace fitPlace,
                  GatherType gatherType, PermitGender permitGender, PermitAges permitAges,
                  Integer permitPeopleCnt, Long writerId) {

        return innerBuilder().fitCategory(fitCategory).title(title).introduction(introduction)
                .mateAt(mateAt).fitPlace(fitPlace).gatherType(gatherType).permitGender(permitGender)
                .permitAges(permitAges).permitPeopleCnt(permitPeopleCnt).writerId(writerId);
    }

    public void updateIntroImages(Set<Long> introImages) {
        this.introImages = introImages;
    }
}
