package com.fitmate.adapter.out.persistence.redis.notice.repository;

import com.fitmate.adapter.out.persistence.redis.notice.entity.NoticeRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends CrudRepository<NoticeRedisEntity, Long> {
    List<NoticeRedisEntity> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);
    void deleteAllByAccountId(Long accountId);
}
