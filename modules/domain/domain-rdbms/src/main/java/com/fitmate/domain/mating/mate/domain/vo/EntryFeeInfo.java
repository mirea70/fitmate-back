package com.fitmate.domain.mating.mate.domain.vo;

import com.fitmate.domain.converter.mate.GatherSetConverter;
import com.fitmate.domain.converter.mate.OperateFeeSetConverter;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EntryFeeInfo {

    @Column
    private Integer entryFee;

    @Column(name = "operateFees")
    @Convert(converter = OperateFeeSetConverter.class)
    private OperateFeeSet operateFeeSet;

    @Column(name = "gatherFees")
    @Convert(converter = GatherSetConverter.class)
    private GatherFeeSet gatherFeeSet;

    @Column(name = "etc_fees")
    private String etc;
}
