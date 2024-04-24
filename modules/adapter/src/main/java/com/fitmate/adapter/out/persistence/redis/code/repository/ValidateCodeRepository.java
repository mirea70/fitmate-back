package com.fitmate.adapter.out.persistence.redis.code.repository;

import com.fitmate.adapter.out.persistence.redis.code.entity.ValidateCodeRedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface ValidateCodeRepository extends CrudRepository<ValidateCodeRedisEntity, String> {
}
