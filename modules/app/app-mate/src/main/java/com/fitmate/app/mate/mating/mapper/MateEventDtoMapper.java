package com.fitmate.app.mate.mating.mapper;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface MateEventDtoMapper {
    MateEventDtoMapper INSTANCE = Mappers.getMapper(MateEventDtoMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "accountId", source = "accountId")
    MateEventDto.Request toEvent(String title, Long accountId);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "accountIds", source = "accountIds")
    MateEventDto.Approve toEvent(String title, Set<Long> accountIds);
}
