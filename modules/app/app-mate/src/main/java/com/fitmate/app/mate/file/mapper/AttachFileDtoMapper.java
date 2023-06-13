package com.fitmate.app.mate.file.mapper;

import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.domain.file.entity.AttachFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttachFileDtoMapper {
    AttachFileDtoMapper INSTANCE = Mappers.getMapper(AttachFileDtoMapper.class);

    AttachFileDto.Response toResponse(AttachFile attachFile);
}
