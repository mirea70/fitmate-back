package com.fitmate.adapter.out.persistence.jpa.job.entity;

import com.fitmate.adapter.out.persistence.jpa.job.JobStatus;
import com.fitmate.adapter.out.persistence.jpa.job.JobType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_queue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JobQueueJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Column(nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private int retryCount;

    @Column(length = 1000)
    private String errorMessage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    public JobQueueJpaEntity(JobType jobType, Long targetId) {
        this.jobType = jobType;
        this.targetId = targetId;
        this.status = JobStatus.PENDING;
        this.retryCount = 0;
        this.createdAt = LocalDateTime.now();
    }

    public void markProcessing() {
        this.status = JobStatus.PROCESSING;
    }

    public void markCompleted() {
        this.status = JobStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
        this.errorMessage = null;
    }

    public void markFailed(String errorMessage) {
        this.status = JobStatus.FAILED;
        this.retryCount++;
        this.processedAt = LocalDateTime.now();
        this.errorMessage = abbreviate(errorMessage);
    }

    private String abbreviate(String message) {
        if (message == null) return null;
        return message.length() <= 1000 ? message : message.substring(0, 1000);
    }
}
