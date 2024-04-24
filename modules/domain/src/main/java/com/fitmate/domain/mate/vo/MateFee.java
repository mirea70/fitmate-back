package com.fitmate.domain.mate.vo;

import lombok.Getter;

@Getter
public class MateFee {

    private final Long mateId;
    private String name;
    private Integer fee;
    public MateFee(Long mateId, String name, Integer fee) {
        this.mateId = mateId;
        this.fee = fee;
        this.name = name;
    }
}
