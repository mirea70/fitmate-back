package com.fitmate.adapter.out.persistence.jpa.mate.entity;

import com.fitmate.adapter.out.persistence.jpa.common.BaseJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mate_request")
@SQLDelete(sql = "UPDATE MATE_REQUEST SET DELETED_AT = CURRENT_TIMESTAMP WHERE ID = ? ")
@Where(clause = "DELETED_AT IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MateRequestJpaEntity extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String comeAnswer;

    @Column(nullable = false)
    private Long mateId;

    @Column(nullable = false)
    private Long applierId;

    @Column(nullable = false)
    private String approveStatus;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public MateRequestJpaEntity(Long id, String comeAnswer, Long mateId, String approveStatus, Long applierId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.comeAnswer = comeAnswer;
        this.mateId = mateId;
        this.approveStatus = approveStatus;
        this.applierId = applierId;
        super.createdAt = createdAt;
        super.updatedAt = updatedAt;
    }
}
