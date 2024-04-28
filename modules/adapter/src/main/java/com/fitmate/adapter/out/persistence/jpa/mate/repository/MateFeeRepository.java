package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateFeeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateFeeRepository extends JpaRepository<MateFeeJpaEntity, Long> {
    List<MateFeeJpaEntity> findAllByMateId(Long mateId);
    void deleteAllByMateId(Long mateId);
}
