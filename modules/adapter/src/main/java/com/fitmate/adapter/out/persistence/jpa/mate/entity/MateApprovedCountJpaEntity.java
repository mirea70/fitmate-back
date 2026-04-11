package com.fitmate.adapter.out.persistence.jpa.mate.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "mate_approved_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MateApprovedCountJpaEntity {

    @Id
    @Column(name = "mate_id")
    private Long mateId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int approvedCount;

    @Version
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long version = 0L;

    public MateApprovedCountJpaEntity(Long mateId) {
        this.mateId = mateId;
        this.approvedCount = 0;
    }

    public void increment() {
        this.approvedCount++;
    }

    public void decrement() {
        if (this.approvedCount > 0) this.approvedCount--;
    }
}
