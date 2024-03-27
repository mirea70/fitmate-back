package com.fitmate.domain.mating.mate.domain.entity;

import com.fitmate.domain.base.BaseDomain;
import com.fitmate.domain.converter.SetConverter;
import com.fitmate.domain.mating.mate.domain.enums.FitCategory;
import com.fitmate.domain.mating.mate.domain.enums.GatherType;
import com.fitmate.domain.mating.mate.domain.enums.PermitGender;
import com.fitmate.domain.mating.mate.domain.vo.EntryFeeInfo;
import com.fitmate.domain.mating.mate.domain.vo.FitPlace;
import com.fitmate.domain.mating.mate.domain.vo.PermitAges;
import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.exception.LimitException;
import com.fitmate.exceptions.exception.NotMatchException;
import com.fitmate.exceptions.result.DuplicatedErrorResult;
import com.fitmate.exceptions.result.LimitErrorResult;
import com.fitmate.exceptions.result.NotMatchErrorResult;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
@SuperBuilder(builderMethodName = "innerBuilder")
@SQLDelete(sql = "UPDATE MATING SET DELETED_AT = CURRENT_TIMESTAMP WHERE MATING_ID = ? ")
@Where(clause = "DELETED_AT IS NULL")
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

    @Column(columnDefinition = "CLOB")
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
    @ColumnDefault("0")
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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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

    public void updateWriterId(Long writerId){
        if (writerId == null) return;
        this.writerId = writerId;
    }

    public void addWaitingAccountId(Long accountId) {
        if(accountId == null) return;
        if(this.waitingAccountIds == null) this.waitingAccountIds = new HashSet<>();

        this.waitingAccountIds.add(accountId);
        this.waitingAccountCnt = this.waitingAccountIds.size();
    }

    public void addApprovedAccountIds(Long accountId) {
        if(accountId == null) return;
        if(this.approvedAccountIds == null) this.approvedAccountIds = new HashSet<>();

        this.approvedAccountIds.add(accountId);
        this.approvedAccountCnt = this.approvedAccountIds.size();
    }

//    public void removeApplier(Long accountId) {
//        if(accountId == null) return;
//        this.waitingAccountIds.remove(accountId);
//        this.waitingAccountCnt = this.waitingAccountIds.size();
//        this.approvedAccountIds.remove(accountId);
//        this.approvedAccountCnt = this.approvedAccountIds.size();
//    }

    public void forApproveRequest(Set<Long> accountIds) {
        if(accountIds == null || accountIds.isEmpty()) return;
        checkAccountIds(accountIds);

        if(this.approvedAccountIds == null) this.approvedAccountIds = new HashSet<>();
        this.waitingAccountIds.removeAll(accountIds);
        this.waitingAccountCnt = this.waitingAccountIds.size();
        this.approvedAccountIds.addAll(accountIds);
        this.approvedAccountCnt = this.approvedAccountIds.size();
    }

    private void checkAccountIds(Set<Long> accountIds) {
        checkApproveIdsDuplicate(accountIds);
        checkExistWaitingAccountIds(accountIds);
        checkLimitPeople(accountIds);
    }

    private void checkExistWaitingAccountIds(Set<Long> accountIds) {
        if(this.waitingAccountIds == null || this.waitingAccountIds.isEmpty()) return;
        if(!this.waitingAccountIds.containsAll(accountIds))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WAIT_ACCOUNT_LIST);
    }

    private void checkLimitPeople(Set<Long> accountIds) {
        if(this.waitingAccountCnt + accountIds.size() > this.permitPeopleCnt)
            throw new LimitException(LimitErrorResult.OVER_MATE_PEOPLE_LIMIT);
    }

    private void checkApproveIdsDuplicate(Set<Long> accountIds) {
        if(this.approvedAccountIds != null && !this.approvedAccountIds.isEmpty()) {
            for(Long accountId : accountIds) {
                if(this.approvedAccountIds.contains(accountId)) throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_MATE_APPLIER);
            }
        }
    }
}
