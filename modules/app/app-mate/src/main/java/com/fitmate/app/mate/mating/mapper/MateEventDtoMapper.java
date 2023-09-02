package com.fitmate.app.mate.mating.mapper;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MateEventDtoMapper {
    MateEventDtoMapper INSTANCE = Mappers.getMapper(MateEventDtoMapper.class);

    @Mapping(target = "matingId", source = "matingId")
    @Mapping(target = "accountId", source = "accountId")
    MateEventDto.Request toEvent(Long matingId, Long accountId);
}
