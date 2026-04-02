package com.fitmate.adapter.out.persistence.redis.notice.mapper;

import com.fitmate.adapter.out.persistence.redis.notice.entity.NoticeRedisEntity;
import com.fitmate.domain.notice.Notice;
import com.fitmate.port.out.notice.NoticeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticePersistenceMapper {
    public NoticeRedisEntity domainToEntity(Notice notice) {
        return new NoticeRedisEntity(
                notice.getAccountId(),
                notice.getMateId(),
                notice.getSenderAccountId(),
                notice.getContent(),
                notice.getNoticeType().name(),
                notice.getExpiration()
        );
    }

    public NoticeResponse entityToResponse(NoticeRedisEntity entity) {
        return new NoticeResponse(
                entity.getId(),
                entity.getMatingId(),
                entity.getSenderAccountId(),
                entity.getContent(),
                entity.getNoticeType(),
                entity.getCreatedAt()
        );
    }

    public List<NoticeResponse> entitiesToResponses(List<NoticeRedisEntity> noticeEntities) {
        return noticeEntities.stream()
                .map(this::entityToResponse)
                .toList();
    }
}
