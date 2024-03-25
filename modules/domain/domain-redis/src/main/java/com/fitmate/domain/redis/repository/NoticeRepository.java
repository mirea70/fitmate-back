package com.fitmate.domain.redis.repository;

import com.fitmate.domain.redis.entity.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {
    List<Notice> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);
}
