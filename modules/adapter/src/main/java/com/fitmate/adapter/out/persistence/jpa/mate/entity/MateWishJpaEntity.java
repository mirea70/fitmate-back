package com.fitmate.adapter.out.persistence.jpa.mate.entity;

import com.fitmate.adapter.out.persistence.jpa.common.BaseJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mate_wish", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account_id", "mate_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MateWishJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "mate_id", nullable = false)
    private Long mateId;

    public MateWishJpaEntity(Long accountId, Long mateId) {
        this.accountId = accountId;
        this.mateId = mateId;
    }
}
