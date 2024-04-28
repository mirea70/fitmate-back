package com.fitmate.domain.mate;

import lombok.Getter;

@Getter
public class MateId {
    private final Long value;

    public MateId(Long value) {
        this.value = value;
    }
}

