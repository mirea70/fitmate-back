package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApplyJpaEntity;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MateApplyRepository extends JpaRepository<MateApplyJpaEntity, Long> {
    boolean existsByMateIdAndApplierId(Long mateId, Long applierId);

    Optional<MateApplyJpaEntity> findByMateIdAndApplierId(Long mateId, Long applierId);

    default MateApplyJpaEntity getByMateAndApplier(Long mateId, Long applierId) {
        return this.findByMateIdAndApplierId(mateId, applierId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATE_REQUEST_DATA));
    }
    void deleteAllByApplierId(Long applierId);
}
