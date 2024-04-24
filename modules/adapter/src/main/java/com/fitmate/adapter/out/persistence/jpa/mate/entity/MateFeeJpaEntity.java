package com.fitmate.adapter.out.persistence.jpa.mate.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mate_fee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MateFeeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private Long mateId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer fee;

    public MateFeeJpaEntity(Long id, Long mateId, String name, Integer fee) {
        this.id = id;
        this.mateId = mateId;
        this.fee = fee;
        this.name = name;
    }
}
