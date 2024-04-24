package com.fitmate.domain.mate.vo;

import lombok.Getter;

@Getter
public class MateRequestId {
    private final Long value;

    public MateRequestId(Long value) {
        this.value = value;
    }
}
