package com.fitmate.app.mate.notice.mapper;

import com.fitmate.app.mate.notice.dto.NoticeDto;
import com.fitmate.domain.redis.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeDtoMapper {
    NoticeDtoMapper INSTANCE = Mappers.getMapper(NoticeDtoMapper.class);

    NoticeDto.Response toResponse(Notice notice);

    List<NoticeDto.Response> toResponses(List<Notice> notices);
}
