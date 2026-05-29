package com.fitmate.adapter.out.persistence.jpa.job.scheduler;

import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.job.JobStatus;
import com.fitmate.adapter.out.persistence.jpa.job.JobType;
import com.fitmate.adapter.out.persistence.jpa.job.entity.JobQueueJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.job.repository.JobQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageResizingJobScheduler {

    private static final int MAX_RETRY_COUNT = 3;

    private final JobQueueRepository jobQueueRepository;
    private final AttachFilePersistenceAdapter attachFilePersistenceAdapter;

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void processImageResizingJobs() {
        List<JobQueueJpaEntity> jobs = jobQueueRepository
                .findTop20ByJobTypeAndStatusInAndRetryCountLessThanOrderByCreatedAtAsc(
                        JobType.IMAGE_RESIZING,
                        List.of(JobStatus.PENDING, JobStatus.FAILED),
                        MAX_RETRY_COUNT
                );

        for (JobQueueJpaEntity job : jobs) {
            process(job);
        }
    }

    private void process(JobQueueJpaEntity job) {
        job.markProcessing();
        try {
            attachFilePersistenceAdapter.createThumbnail(job.getTargetId());
            job.markCompleted();
        } catch (Exception e) {
            job.markFailed(e.getMessage());
            log.warn("Image resizing job failed. jobId={}, targetId={}, retryCount={}",
                    job.getId(), job.getTargetId(), job.getRetryCount(), e);
        }
    }
}
