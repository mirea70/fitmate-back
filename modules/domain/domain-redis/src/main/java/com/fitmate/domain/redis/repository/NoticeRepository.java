package com.fitmate.domain.redis.repository;

import com.fitmate.domain.redis.entity.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {
}
