package com.fitmate.adapter.out.persistence.redis.notice.mapper;

import com.fitmate.adapter.out.persistence.redis.notice.entity.NoticeRedisEntity;
import com.fitmate.domain.notice.Notice;
import com.fitmate.port.out.notice.NoticeResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticePersistenceMapper {
    public NoticeRedisEntity domainToEntity(Notice notice) {
        return new NoticeRedisEntity(notice.getAccountId(), notice.getContent(),
                notice.getMateId(), notice.getExpiration());
    }

    public NoticeResponse entityToResponse(NoticeRedisEntity noticeEntity) {
        return new NoticeResponse(noticeEntity.getId(), noticeEntity.getMatingId(), noticeEntity.getContent());
    }

    public List<NoticeResponse> entitiesToResponses(List<NoticeRedisEntity> noticeEntities) {
        return noticeEntities.stream()
                .map(this::entityToResponse)
                .toList();
    }
}
