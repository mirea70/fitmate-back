package com.fitmate.domain.redis.repository;

import com.fitmate.domain.redis.entity.ValidateCode;
import org.springframework.data.repository.CrudRepository;

public interface ValidateCodeRepository extends CrudRepository<ValidateCode, String> {
}
