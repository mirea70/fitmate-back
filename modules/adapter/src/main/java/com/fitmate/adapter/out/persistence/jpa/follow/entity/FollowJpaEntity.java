package com.fitmate.adapter.out.persistence.jpa.follow.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@NoArgsConstructor
@Getter
public class FollowJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long fromAccountId;
    @Column(nullable = false)
    private Long toAccountId;

    public FollowJpaEntity(Long fromAccountId, Long toAccountId) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }
}
