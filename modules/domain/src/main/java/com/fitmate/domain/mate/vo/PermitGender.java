package com.fitmate.domain.mate.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermitGender {
    ALL("누구나"),
    MALE("남성만"),
    FEMALE("여성만"),
    ;

    private final String name;
}
