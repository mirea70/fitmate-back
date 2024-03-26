package com.fitmate.domain.mating.request.domain.repository;

import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MateRequestRepository extends JpaRepository<MateRequest, Long> {
    boolean existsByMatingIdAndAccountId(Long matingId, Long accountId);
    List<MateRequest> findAllByMatingIdAndAccountIdIn(Long matingId, Set<Long> accountIds);
    void deleteAllByAccountId(Long accountId);
}
