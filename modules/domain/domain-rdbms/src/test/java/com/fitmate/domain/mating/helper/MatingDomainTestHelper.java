package com.fitmate.domain.mating.helper;

import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.domain.enums.*;
import com.fitmate.domain.mating.domain.vo.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Component
public class MatingDomainTestHelper {

    public Mating getTestMatingNotEntryFeeInfo() {
        FitPlace fitPlace = FitPlace.builder()
                .name("엑스콩고휘트니스")
                .address("서울 동작구 노량진동 333-33번지")
                .build();

        PermitAges permitAges = PermitAges.builder()
                .max(30)
                .min(20)
                .build();

        return Mating.builder(FitCategory.FITNESS, "헬스갈 사람","소개이다",LocalDateTime.now(),
                        fitPlace, GatherType.FAST, PermitGender.ALL,permitAges,3, 1L)
                .introImages(Set.of(1L,2L))
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
                .build();
    }
}
