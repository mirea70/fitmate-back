package com.fitmate.domain.mating.request.domain.repository;

import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateRequestRepository extends JpaRepository<MateRequest, Long> {
}
