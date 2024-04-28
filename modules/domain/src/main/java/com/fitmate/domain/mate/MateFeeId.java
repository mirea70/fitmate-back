package com.fitmate.domain.mate;

import lombok.Getter;

@Getter
public class MateFeeId {
    private final Long value;

    public MateFeeId(Long value) {
        this.value = value;
    }
}

