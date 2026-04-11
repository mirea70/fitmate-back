package com.fitmate.adapter.out.persistence.jpa.mate.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mate_apply_retry_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MateRetryCountJpaEntity {

    @Id
    @Column(name = "mate_id")
    private Long mateId;

    @Column(nullable = false)
    private Long retryCount;

    public MateRetryCountJpaEntity(Long mateId) {
        this.mateId = mateId;
        this.retryCount = 0L;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }
}
