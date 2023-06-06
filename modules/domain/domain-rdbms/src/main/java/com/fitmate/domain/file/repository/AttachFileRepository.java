package com.fitmate.domain.file.repository;

import com.fitmate.domain.file.entity.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
}
