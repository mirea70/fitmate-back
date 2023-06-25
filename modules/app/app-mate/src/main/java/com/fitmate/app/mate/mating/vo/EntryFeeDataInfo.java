package com.fitmate.app.mate.mating.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryFeeDataInfo {
    private Integer entryFee;

    private List<String> operateFees;

    private List<String> gatherFees;

    private String etc;
}
