package com.fitmate.domain.mating.mate.domain.vo;

import com.fitmate.domain.mating.mate.domain.enums.OperateFee;
import lombok.Getter;

import java.util.EnumSet;

@Getter
public class OperateFeeSet {
    private EnumSet<OperateFee> operateFees = EnumSet.noneOf(OperateFee.class);

    public OperateFeeSet(EnumSet<OperateFee> operateFees) {
        this.operateFees.addAll(operateFees);
    }
}
