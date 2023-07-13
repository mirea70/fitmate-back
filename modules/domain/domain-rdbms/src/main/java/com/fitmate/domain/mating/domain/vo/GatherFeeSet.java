package com.fitmate.domain.mating.domain.vo;

import com.fitmate.domain.mating.domain.enums.GatherFee;
import lombok.Getter;

import java.util.EnumSet;

@Getter
public class GatherFeeSet {
    private EnumSet<GatherFee> gatherFees = EnumSet.noneOf(GatherFee.class);

    public GatherFeeSet(EnumSet<GatherFee> gatherFees) { this.gatherFees.addAll(gatherFees); }
}
