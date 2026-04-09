package com.fitmate.domain.mate;

import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.error.results.LimitErrorResult;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
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

    private int approvedCount;

    private LocalDateTime closedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Mate withId(MateId id, FitCategory fitCategory, String title, String introduction, Set<Long> introImageIds, LocalDateTime mateAt, FitPlace fitPlace, GatherType gatherType, PermitGender permitGender, PermitAges permitAges, Integer permitPeopleCnt, Long writerId, List<MateFee> mateFees, String applyQuestion, int approvedCount, LocalDateTime closedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {

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
                approvedCount,
                closedAt,
                null,
                createdAt,
                updatedAt
        );
    }

    public static Mate withoutId(FitCategory fitCategory, String title, String introduction, Set<Long> introImageIds, LocalDateTime mateAt, FitPlace fitPlace, GatherType gatherType, PermitGender permitGender, PermitAges permitAges, Integer permitPeopleCnt, Long writerId, List<MateFee> mateFees, String applyQuestion) {

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
                0, // 승인자 카운트 (작성자 미포함, 표시 시 +1)
                null,
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

    public void checkCapacity() {
        if (isFull())
            throw new LimitException(LimitErrorResult.OVER_MATE_PEOPLE_LIMIT);
    }

    public void incrementApprovedCount() {
        checkCapacity();
        this.approvedCount++;
    }

    public void decrementApprovedCount() {
        if (this.approvedCount > 0)
            this.approvedCount--;
    }

    public boolean isClosed() {
        return this.closedAt != null;
    }

    public void close() {
        if (this.closedAt != null) return;
        this.closedAt = LocalDateTime.now();
    }

    public boolean isFull() {
        return (this.approvedCount + 1) >= this.permitPeopleCnt; // +1은 작성자
    }

    public boolean isPastMateAt() {
        return this.mateAt != null && this.mateAt.isBefore(LocalDateTime.now());
    }
}
