package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRepository extends JpaRepository<MateJpaEntity, Long> {

    @NotNull
    default MateJpaEntity getById(@NonNull Long id) {
        return this.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATE_DATA));
    }

    void deleteAllByWriterId(Long writerId);
}
