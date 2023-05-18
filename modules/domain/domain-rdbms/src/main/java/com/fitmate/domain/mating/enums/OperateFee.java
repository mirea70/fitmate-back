package com.fitmate.domain.mating.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperateFee {
    HOST_FEE("호스트 수고비"),
    AVOID_NO_SHOW("노쇼 방지비"),
    PLATFORM_FEE("플랫폼 수수료");

    private final String description;
}
