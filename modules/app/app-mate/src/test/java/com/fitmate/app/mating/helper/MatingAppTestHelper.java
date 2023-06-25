package com.fitmate.app.mating.helper;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.vo.EntryFeeDataInfo;
import com.fitmate.domain.mating.entity.Mating;
import com.fitmate.domain.mating.enums.*;
import com.fitmate.domain.mating.vo.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
public class MatingAppTestHelper {

    public MatingDto.Create getTestRequest() throws Exception {
        FitPlace fitPlace = FitPlace.builder()
                .name("엑스콩고휘트니스")
                .address("서울 동작구 노량진동 333-33번지")
                .build();

        PermitAges permitAges = PermitAges.builder()
                .max(30)
                .min(20)
                .build();

        EntryFeeDataInfo entryFeeDataInfo = EntryFeeDataInfo.builder()
                .entryFee(10000)
                .gatherFees(List.of("RENTAL_FEE"))
                .operateFees(List.of("PLATFORM_FEE", "HOST_FEE", "AVOID_NO_SHOW"))
                .build();


        return MatingDto.Create.builder()
                .fitCategory(FitCategory.FITNESS)
                .title("헬스갈 사람")
                .introduction("꼭오세요")
                .mateAt(LocalDateTime.of(2023,06,30,15,10,10))
                .fitPlace(fitPlace)
                .gatherType(GatherType.FAST)
                .permitGender(PermitGender.ALL)
                .permitAges(permitAges)
                .permitPeopleCnt(4)
                .writerId(1L)
                .hasEntryFee(true)
                .entryFeeInfo(entryFeeDataInfo)
                .build();
    }

    public MatingDto.Response getTestResponse() {
        FitPlace fitPlace = FitPlace.builder()
                .name("엑스콩고휘트니스")
                .address("서울 동작구 노량진동 333-33번지")
                .build();

        PermitAges permitAges = PermitAges.builder()
                .max(30)
                .min(20)
                .build();

        EntryFeeDataInfo entryFeeDataInfo = EntryFeeDataInfo.builder()
                .entryFee(10000)
                .gatherFees(List.of("RENTAL_FEE"))
                .operateFees(List.of("PLATFORM_FEE", "HOST_FEE", "AVOID_NO_SHOW"))
                .build();

        return MatingDto.Response.builder()
                .id(1L)
                .fitCategory(FitCategory.FITNESS)
                .title("헬스갈 사람")
                .introduction("꼭오세요")
                .mateAt(LocalDateTime.now())
                .fitPlace(fitPlace)
                .gatherType(GatherType.FAST)
                .permitGender(PermitGender.ALL)
                .permitAges(permitAges)
                .permitPeopleCnt(4)
                .writerNickName("길동")
                .hasEntryFee(true)
                .entryFeeInfo(entryFeeDataInfo)
                .introImages(List.of(1L, 2L))
                .build();
    }

    public Mating getTestMating() {
        FitPlace fitPlace = FitPlace.builder()
                .name("엑스콩고휘트니스")
                .address("서울 동작구 노량진동 333-33번지")
                .build();

        PermitAges permitAges = PermitAges.builder()
                .max(30)
                .min(20)
                .build();

        EnumSet<GatherFee> gatherFees = EnumSet.of(GatherFee.RENTAL_FEE);
        GatherFeeSet gatherFeeSet = new GatherFeeSet(gatherFees);

        EnumSet<OperateFee> operateFees = EnumSet.of(OperateFee.PLATFORM_FEE, OperateFee.HOST_FEE, OperateFee.AVOID_NO_SHOW);
        OperateFeeSet operateFeeSet = new OperateFeeSet(operateFees);

        EntryFeeInfo entryFeeInfo = EntryFeeInfo.builder()
                .entryFee(10000)
                .gatherFeeSet(gatherFeeSet)
                .operateFeeSet(operateFeeSet)
                .build();

        return Mating.builder(FitCategory.FITNESS, "헬스갈 사람","소개이다",LocalDateTime.now(),
                        fitPlace,GatherType.FAST,PermitGender.ALL,permitAges,3, 1L)
                .introImages(Set.of(1L,2L))
                .hasEntryFee(true)
                .entryFeeInfo(entryFeeInfo)
                .id(1L)
                .build();
    }
}
