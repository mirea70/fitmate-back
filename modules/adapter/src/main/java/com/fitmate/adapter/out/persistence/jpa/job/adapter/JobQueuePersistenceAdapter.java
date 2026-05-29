package com.fitmate.adapter.out.persistence.jpa.job.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.job.JobType;
import com.fitmate.adapter.out.persistence.jpa.job.entity.JobQueueJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.job.repository.JobQueueRepository;
import com.fitmate.port.out.job.LoadJobQueuePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class JobQueuePersistenceAdapter implements LoadJobQueuePort {

    private final JobQueueRepository jobQueueRepository;

    @Override
    public void enqueueImageResizingJobs(Collection<Long> attachFileIds) {
        if (attachFileIds == null || attachFileIds.isEmpty()) return;

        List<JobQueueJpaEntity> jobs = attachFileIds.stream()
                .map(attachFileId -> new JobQueueJpaEntity(JobType.IMAGE_RESIZING, attachFileId))
                .toList();
        jobQueueRepository.saveAll(jobs);
    }
}
