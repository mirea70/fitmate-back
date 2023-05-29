package com.fitmate.domain.mating.converter;

import com.fitmate.domain.mating.enums.GatherFee;
import com.fitmate.domain.mating.enums.OperateFee;
import com.fitmate.domain.mating.vo.GatherFeeSet;
import com.fitmate.domain.mating.vo.OperateFeeSet;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class GatherSetConverter implements AttributeConverter<GatherFeeSet, String> {

    @Override
    public String convertToDatabaseColumn(GatherFeeSet attribute) {
        if (attribute == null) return null;
        return attribute.getGatherFees().stream()
                .map(GatherFee::getDescription)
                .collect(Collectors.joining(","));
    }

    @Override
    public GatherFeeSet convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String[] operateFees = dbData.split(",");
        EnumSet<GatherFee> gatherFeeSet = Arrays.stream(operateFees)
                .map(GatherFee::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(GatherFee.class)));

        return new GatherFeeSet(gatherFeeSet);
    }
}
