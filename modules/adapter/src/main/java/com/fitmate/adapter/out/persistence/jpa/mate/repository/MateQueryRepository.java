package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.dto.*;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.port.in.mate.command.MateListCommand;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.fitmate.adapter.out.persistence.jpa.account.entity.QAccountJpaEntity.accountJpaEntity;
import static com.fitmate.adapter.out.persistence.jpa.mate.entity.QMateJpaEntity.mateJpaEntity;

@Repository
@RequiredArgsConstructor
public class MateQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<MateSimpleJpaResponse> readList(MateListCommand command) {

        return queryFactory
                .select(new QMateSimpleJpaResponse(
                        mateJpaEntity.id,
                        mateJpaEntity.introImageIds,
                        accountJpaEntity.profileImageId,
                        accountJpaEntity.nickName,
                        mateJpaEntity.fitCategory,
                        mateJpaEntity.title,
                        mateJpaEntity.fitPlaceAddress,
                        mateJpaEntity.mateAt,
                        mateJpaEntity.gatherType,
                        mateJpaEntity.permitGender,
                        mateJpaEntity.permitPeopleCnt,
                        mateJpaEntity.approvedAccountIds
                ))
                .from(mateJpaEntity)
                .innerJoin(accountJpaEntity).on(mateJpaEntity.writerId.eq(accountJpaEntity.id))
                .where(filter(command))
                .offset(command.getPage())
                .limit(command.getSize() + 1)
                .fetch();
    }

//    private BooleanExpression afterLastMatingId(Long lastMatingId) {
//        return lastMatingId != null ? mateJpaEntity.id.gt(lastMatingId) : null;
//    }
    private BooleanBuilder filter(MateListCommand command) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(eqDayOfWeek(command.getDayOfWeek()));
        booleanBuilder.and(inDateRange(command.getStartMateAt(), command.getEndMateAt()));
        booleanBuilder.and(containRegions(command.getFitPlaceRegions()));
        booleanBuilder.and(inPermitAges(command.getPermitMinAge(), command.getPermitMaxAge()));
        booleanBuilder.and(inPermitPerson(command.getStartLimitPeopleCnt(), command.getEndLimitPeopleCnt()));
        booleanBuilder.and(eqFitCategory(command.getFitCategory()));

        booleanBuilder.and(filterByKeyword(command.getKeyword()));
        return booleanBuilder;
    }

    private BooleanExpression eqDayOfWeek(Integer dayOfWeek) {
        if(dayOfWeek == null) return null;
        if(dayOfWeek < 0 || dayOfWeek > 6) return null;

        String dayOfWeekString = String.valueOf(dayOfWeek+1);
        return Expressions.stringTemplate("TO_CHAR({0}, 'D')", mateJpaEntity.mateAt).eq(dayOfWeekString);
    }

    private BooleanExpression inDateRange(LocalDateTime startAt, LocalDateTime endAt) {
        // 둘다 null일 경우
        if(startAt == null && endAt == null) return null;
        // 둘 중 하나가 null일 경우 -> 해당날짜 00시 ~ 23:59 처리
        else if(startAt == null || endAt == null) {
            LocalDateTime target = startAt != null ? startAt : endAt;
            return mateJpaEntity.mateAt.between(target.with(LocalTime.MIN), target.with(LocalTime.MAX));
        }
        // 둘 다 null이 아닐 경우
        else return mateJpaEntity.mateAt.between(startAt.with(LocalTime.MIN), endAt.with(LocalTime.MAX));
    }

    private BooleanBuilder containRegions(List<String> regions) {
        if(regions == null || regions.size() == 0) return null;
        // 3개 넘어가면 처음으로부터 3개까지만 적용
        BooleanBuilder regionsBooleanBuilder = new BooleanBuilder();
        int count = 0;
        for(String region : regions) {
            if(count >= 3) break;
            regionsBooleanBuilder.or(containRegion(region));
            count++;
        }
        return regionsBooleanBuilder;
    }

    private BooleanBuilder containRegion(String region) {
        if(region == null || region.isEmpty()) return null;
        String[] arr = region.split(" ");

        BooleanBuilder regionBooleanBuilder = new BooleanBuilder();
        for(String element : arr)
            regionBooleanBuilder.and(mateJpaEntity.fitPlaceAddress.contains(element));
        return regionBooleanBuilder;
    }

    private BooleanBuilder inPermitAges(Integer permitMinAge, Integer permitMaxAge) {
        // 둘 다 null 아닐 경우에만 허용
        if(permitMinAge == null || permitMaxAge == null) return null;

        BooleanBuilder permitAgesBooleanBuilder = new BooleanBuilder();
        permitAgesBooleanBuilder.and(mateJpaEntity.permitMinAge.goe(permitMinAge));
        permitAgesBooleanBuilder.and(mateJpaEntity.permitMaxAge.loe(permitMaxAge));
        return permitAgesBooleanBuilder;
    }

    private BooleanExpression inPermitPerson(Integer startCnt, Integer endCnt) {
        // 둘 다 null 아닐 경우에만 허용
        if(startCnt == null || endCnt == null) return null;
        return mateJpaEntity.permitPeopleCnt.between(startCnt, endCnt);
    }

    private BooleanExpression eqFitCategory(FitCategory fitCategory) {
        if(fitCategory == null) return null;
        return mateJpaEntity.fitCategory.eq(fitCategory.name());
    }

    /**
     *
     * @param keyword
     * 검색 포함 필드 (제목, 지역, 운동 카테고리)
     */
    private BooleanBuilder filterByKeyword(String keyword) {
        if(keyword == null || keyword.isEmpty()) return null;
        BooleanBuilder keywordBooleanBuilder = new BooleanBuilder();
        keywordBooleanBuilder.or(containTitle(keyword));
        keywordBooleanBuilder.or(containAddress(keyword));

        return keywordBooleanBuilder;
    }

    private BooleanExpression containTitle(String keyword) {
        return mateJpaEntity.title.contains(keyword);
    }

    private BooleanExpression containAddress(String keyword) {
        return mateJpaEntity.fitPlaceAddress.contains(keyword);
    }

    // TODO: 방법 모색 필요 (아직 사용 X)
    private BooleanExpression containCategory(String keyword) {
        return mateJpaEntity.fitCategory.contains(keyword);
    }

    public MateQuestionJpaResponse readQuestion(Long mateId) {
        return queryFactory
                .select(new QMateQuestionJpaResponse(
                        accountJpaEntity.profileImageId,
                        accountJpaEntity.nickName,
                        mateJpaEntity.applyQuestion))
                .from(mateJpaEntity)
                .innerJoin(accountJpaEntity).on(mateJpaEntity.writerId.eq(accountJpaEntity.id))
                .where(mateJpaEntity.id.eq(mateId))
                .fetchOne();
    }

    public List<MateApplySimpleJpaResponse> getMyMateRequests(List<Long> matingIds) {
        return null;
//        return queryFactory
//                .select(new QMateRequestSimpleJpaResponse(
//                        mateJpaEntity.id,
//                        mateJpaEntity.introImageIds,
//                        mateJpaEntity.title,
//                        mateJpaEntity.mateAt,
//                        mateJpaEntity.fitPlaceName,
//                        mateJpaEntity.fitPlaceAddress,
//                        mateJpaEntity.permitPeopleCnt,
//                        mateJpaEntity.approvedAccountIds,
//                        mateJpaEntity.totalFee,
//                        mateRequestJpaEntity.createdAt))
//                .from(mateJpaEntity)
//                .innerJoin(mateRequestJpaEntity).on(mateJpaEntity.id.eq(mateRequestJpaEntity.mateId))
//                .where(mateJpaEntity.id.in(matingIds))
//                .orderBy(mateRequestJpaEntity.createdAt.desc())
//                .fetch();
    }
}
