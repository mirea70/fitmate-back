package com.fitmate.domain.mating.mate.domain.enums;

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
