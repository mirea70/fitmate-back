package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateWishJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateWishRepository extends JpaRepository<MateWishJpaEntity, Long> {
    boolean existsByAccountIdAndMateId(Long accountId, Long mateId);
    void deleteByAccountIdAndMateId(Long accountId, Long mateId);
    List<MateWishJpaEntity> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);
    List<MateWishJpaEntity> findAllByMateId(Long mateId);
}
