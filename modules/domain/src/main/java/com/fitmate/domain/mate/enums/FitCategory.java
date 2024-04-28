package com.fitmate.domain.mate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FitCategory {
    FITNESS("헬스"),
    CROSSFIT("크로스핏"),
    ETC("기타"),
    ;

    private final String name;
}
