package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApprovedCountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateApprovedCountRepository extends JpaRepository<MateApprovedCountJpaEntity, Long> {
}
