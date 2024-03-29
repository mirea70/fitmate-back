package com.fitmate.domain.mating.request.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(exclude = {"id", "createAt"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE MateRequest SET DELETED_AT = CURRENT_TIMESTAMP WHERE MATE_REQUEST_ID = ? ")
@Where(clause = "DELETED_AT IS NULL")
public class MateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mate_request_id")
    private Long id;

    @Column(nullable = false)
    @Size(min = 5)
    private String comeAnswer;

    @Column(nullable = false)
    private Long matingId;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatus;

    @Column
    @CreatedDate
    private LocalDateTime createAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public MateRequest(String comeAnswer, Long matingId, ApproveStatus approveStatus, Long accountId) {
        this.comeAnswer = comeAnswer;
        this.matingId = matingId;
        this.approveStatus = approveStatus;
        this.accountId = accountId;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ApproveStatus {
        WAIT("대기중"),
        APPROVE("승인");

        private final String label;
    }
}
