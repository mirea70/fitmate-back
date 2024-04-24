package com.fitmate.adapter.out.persistence.jpa.file.repository;

import com.fitmate.adapter.out.persistence.jpa.file.entity.AttachFileJpaEntity;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AttachFileRepository extends JpaRepository<AttachFileJpaEntity, Long> {

    @NonNull
    default AttachFileJpaEntity getById(@NonNull Long id) {
        return this.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
    }

    List<AttachFileJpaEntity> findAllByIdIn(Set<Long> ids);
}
