package com.fitmate.adapter.out.persistence.jpa.job.repository;

import com.fitmate.adapter.out.persistence.jpa.job.JobStatus;
import com.fitmate.adapter.out.persistence.jpa.job.JobType;
import com.fitmate.adapter.out.persistence.jpa.job.entity.JobQueueJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JobQueueRepository extends JpaRepository<JobQueueJpaEntity, Long> {

    List<JobQueueJpaEntity> findTop20ByJobTypeAndStatusInAndRetryCountLessThanOrderByCreatedAtAsc(
            JobType jobType,
            Collection<JobStatus> statuses,
            int retryCount
    );
}
