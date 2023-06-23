package com.fitmate.domain.mating.converter;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SetConverter implements AttributeConverter<Set<Long>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Long> attribute) {
        if(attribute == null) return null;
        return attribute.stream().map((l) -> String.valueOf(l.longValue()))
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Long> convertToEntityAttribute(String dbData) {
        String[] imageIds = dbData.split(",");
        Set<Long> set = new HashSet<>();
        Arrays.stream(imageIds).forEach((l) -> set.add(Long.valueOf(l)));
        return set;
    }
}
