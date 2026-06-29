package com.fitmate.adapter.out.persistence.jpa.retry.entity;

import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryDomain;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
        name = "retry_count",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_retry_count_target",
                        columnNames = {"domain", "target_id", "type"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RetryCountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RetryDomain domain;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RetryType type;

    @Column(nullable = false)
    private Long retryCount;

    public RetryCountJpaEntity(RetryDomain domain, Long targetId, RetryType type) {
        this.domain = domain;
        this.targetId = targetId;
        this.type = type;
        this.retryCount = 0L;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }
}
