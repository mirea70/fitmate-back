package com.fitmate.domain.mating.domain.repository;

import com.fitmate.domain.mating.domain.entity.Mating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatingRepository extends JpaRepository<Mating, Long> {
}
