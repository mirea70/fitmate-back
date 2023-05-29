package com.fitmate.domain.mating.converter;

import com.fitmate.domain.mating.enums.OperateFee;
import com.fitmate.domain.mating.vo.OperateFeeSet;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class OperateFeeSetConverter implements AttributeConverter<OperateFeeSet, String> {

    @Override
    public String convertToDatabaseColumn(OperateFeeSet attribute) {
        if (attribute == null) return null;
        return attribute.getOperateFees().stream()
                .map(OperateFee::getDescription)
                .collect(Collectors.joining(","));
    }

    @Override
    public OperateFeeSet convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String[] operateFees = dbData.split(",");
        EnumSet<OperateFee> operateFeeSet = Arrays.stream(operateFees)
                .map(OperateFee::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(OperateFee.class)));

        return new OperateFeeSet(operateFeeSet);
    }
}
