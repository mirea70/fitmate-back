package com.fitmate.domain.mate;

import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.error.exceptions.NotMatchException;
import com.fitmate.domain.error.results.DuplicatedErrorResult;
import com.fitmate.domain.error.results.LimitErrorResult;
import com.fitmate.domain.error.results.NotMatchErrorResult;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Mate {

    private final MateId id;

    private FitCategory fitCategory;

    private String title;

    private String introduction;

    private Set<Long> introImageIds;

    private LocalDateTime mateAt;

    private FitPlace fitPlace;

    private GatherType gatherType;

    private PermitGender permitGender;

    private PermitAges permitAges;

    private Integer permitPeopleCnt;

    private final Long writerId;

    private Integer totalFee;

    private List<MateFee> mateFees;

    private String applyQuestion;

    private Set<Long> waitingAccountIds;

    private Set<Long> approvedAccountIds;

    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Mate withId(MateId id, FitCategory fitCategory, String title, String introduction, Set<Long> introImageIds, LocalDateTime mateAt, FitPlace fitPlace, GatherType gatherType, PermitGender permitGender, PermitAges permitAges, Integer permitPeopleCnt, Long writerId, List<MateFee> mateFees, String applyQuestion, Set<Long> waitingAccountIds, Set<Long> approvedAccountIds, LocalDateTime createdAt, LocalDateTime updatedAt) {

        Integer totalFee = mateFees.stream().map(MateFee::getFee).mapToInt(i -> i).sum();
        return new Mate(
                id,
                fitCategory,
                title,
                introduction,
                introImageIds,
                mateAt,
                fitPlace,
                gatherType,
                permitGender,
                permitAges,
                permitPeopleCnt,
                writerId,
                totalFee,
                mateFees,
                applyQuestion,
                waitingAccountIds,
                approvedAccountIds,
                createdAt,
                updatedAt,
                null
        );
    }

    public static Mate withoutId(FitCategory fitCategory, String title, String introduction, Set<Long> introImageIds, LocalDateTime mateAt, FitPlace fitPlace, GatherType gatherType, PermitGender permitGender, PermitAges permitAges, Integer permitPeopleCnt, Long writerId, List<MateFee> mateFees, String applyQuestion, Set<Long> waitingAccountIds, Set<Long> approvedAccountIds) {

        Integer totalFee = mateFees.stream().map(MateFee::getFee).mapToInt(i -> i).sum();
        return new Mate(
                null,
                fitCategory,
                title,
                introduction,
                introImageIds,
                mateAt,
                fitPlace,
                gatherType,
                permitGender,
                permitAges,
                permitPeopleCnt,
                writerId,
                totalFee,
                mateFees,
                applyQuestion,
                waitingAccountIds,
                approvedAccountIds,
                null,
                null,
                null
        );
    }

    public void update(FitCategory fitCategory, String title, String introduction, Set<Long> introImageIds, LocalDateTime mateAt, String fitPlaceName, String firPlaceAddress, GatherType gatherType, PermitGender permitGender, Integer permitMaxAge, Integer permitMinAge, Integer permitPeopleCnt, List<MateFee> mateFees, String applyQuestion) {
        if(fitCategory != null) this.fitCategory = fitCategory;
        if(title != null) this.title = title;
        if(introduction != null) this.introduction = introduction;
        if(introImageIds != null) this.introImageIds = introImageIds;
        if(mateAt != null) this.mateAt = mateAt;
        if(gatherType != null) this.gatherType = gatherType;
        if(permitGender != null) this.permitGender = permitGender;
        if(permitPeopleCnt != null) this.permitPeopleCnt = permitPeopleCnt;
        if(mateFees != null) this.mateFees = mateFees;
        if(applyQuestion != null) this.applyQuestion = applyQuestion;

        this.fitPlace.update(fitPlaceName, firPlaceAddress);
        this.permitAges.update(permitMaxAge, permitMinAge);
    }

    public void updateIntroImages(Set<Long> introImages) {
        if(introImages == null || introImages.isEmpty())
            throw new IllegalArgumentException("삽입할 이미지 파일이 존재해야 합니다.");
        this.introImageIds = introImages;
    }

    public void addWaitingAccountId(Long accountId) {
        if(accountId == null) return;
        if(this.waitingAccountIds == null) this.waitingAccountIds = new HashSet<>();

        this.waitingAccountIds.add(accountId);
    }

    public void addApprovedAccountId(Long accountId) {
        if(accountId == null) return;
        if(this.approvedAccountIds == null) this.approvedAccountIds = new HashSet<>();

        this.approvedAccountIds.add(accountId);
    }

    public void approve(Long accountId) {
        if(this.approvedAccountIds == null) this.approvedAccountIds = new HashSet<>();
        checkForApprove(accountId);

        if(this.waitingAccountIds != null && !this.waitingAccountIds.isEmpty())
            this.waitingAccountIds.remove(accountId);
        this.approvedAccountIds.add(accountId);
    }

    public void autoApproveWriter() {
        if(this.approvedAccountIds == null) this.approvedAccountIds = new HashSet<>();
        this.approvedAccountIds.add(this.writerId);
    }

    private void checkForApprove(Long accountId) {
        if(accountId == null)
            throw new IllegalArgumentException("accountId 값이 존재해야 합니다.");
        checkDuplicateApprove(accountId);
        checkExistWaiting(accountId);
        checkIsOverApprove();
    }

    private void checkDuplicateApprove(Long accountId) {
        if(this.approvedAccountIds == null || this.approvedAccountIds.isEmpty())
            return;
        if(this.approvedAccountIds.contains(accountId))
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_MATE_APPLIER);
    }

    private void checkExistWaiting(Long accountId) {
        if(this.waitingAccountIds == null || this.waitingAccountIds.isEmpty())
            return;
        if(!this.waitingAccountIds.contains(accountId))
            throw new NotMatchException(NotMatchErrorResult.NOT_MATCH_WAIT_ACCOUNT_LIST);
    }

    private void checkIsOverApprove() {
        if(this.approvedAccountIds.size() + 1 > this.permitPeopleCnt)
            throw new LimitException(LimitErrorResult.OVER_MATE_PEOPLE_LIMIT);
    }
}
