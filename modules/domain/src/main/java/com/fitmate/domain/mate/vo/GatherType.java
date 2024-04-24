package com.fitmate.domain.mate.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherType {
    FAST("선착순"),
    AGREE("승인제"),
    ;

    private final String name;
}
