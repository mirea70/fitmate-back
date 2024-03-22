package com.fitmate.domain.mating.mate.domain.repository;

import com.fitmate.domain.mating.mate.domain.entity.Mating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatingRepository extends JpaRepository<Mating, Long> {

    List<Mating> findAllByWriterIdOrderByCreatedAtDesc(Long writerId);
}
