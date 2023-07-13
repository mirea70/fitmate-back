package com.fitmate.domain.mating.converter;

import com.fitmate.domain.mating.domain.enums.GatherFee;
import com.fitmate.domain.mating.domain.vo.GatherFeeSet;

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
        String[] gatherFees = dbData.split(",");
        EnumSet<GatherFee> gatherFeeSet = Arrays.stream(gatherFees)
                .map(GatherFee::getValueFromDescription)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(GatherFee.class)));

        return new GatherFeeSet(gatherFeeSet);
    }
}
