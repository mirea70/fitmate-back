package com.fitmate.domain.mating.mate.domain.entity;

import com.fitmate.domain.base.BaseDomain;
import com.fitmate.domain.mating.mate.converter.SetConverter;
import com.fitmate.domain.mating.mate.domain.enums.FitCategory;
import com.fitmate.domain.mating.mate.domain.enums.GatherType;
import com.fitmate.domain.mating.mate.domain.enums.PermitGender;
import com.fitmate.domain.mating.mate.domain.vo.EntryFeeInfo;
import com.fitmate.domain.mating.mate.domain.vo.FitPlace;
import com.fitmate.domain.mating.mate.domain.vo.PermitAges;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    @ColumnDefault("1")
    private Integer permitPeopleCnt = 1;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private boolean hasEntryFee = false;

    @Embedded
    @Builder.Default
    private EntryFeeInfo entryFeeInfo = null;

    @Column(nullable = false)
    @Size(min = 5)
    private String comeQuestion;

    @Column
    @Convert(converter = SetConverter.class)
    @Builder.Default
    private Set<Long> waitingAccountIds = new HashSet<>();

    @Column
    @Convert(converter = SetConverter.class)
    @Builder.Default
    private Set<Long> approvedAccountIds = new HashSet<>();

    @Column
    @ColumnDefault("0")
    @Builder.Default
    private int waitingAccountCnt = 0;

    @Column
    @ColumnDefault("0")
    @Builder.Default
    private int approvedAccountCnt = 0;

    public static MatingBuilder builder(FitCategory fitCategory, String title, String introduction,
                  LocalDateTime mateAt, FitPlace fitPlace,
                  GatherType gatherType, PermitGender permitGender, PermitAges permitAges,
                  Integer permitPeopleCnt, Long writerId, String comeQuestion) {

        return innerBuilder().fitCategory(fitCategory).title(title).introduction(introduction)
                .mateAt(mateAt).fitPlace(fitPlace).gatherType(gatherType).permitGender(permitGender)
                .permitAges(permitAges).permitPeopleCnt(permitPeopleCnt).writerId(writerId)
                .comeQuestion(comeQuestion);
    }

    public void updateIntroImages(Set<Long> introImages) {
        this.introImages = introImages;
    }

    public void addWaitingAccountId(Long accountId) {
        if(accountId == null) return;
        this.waitingAccountIds.add(accountId);
        this.waitingAccountCnt = this.waitingAccountIds.size();
    }
}
