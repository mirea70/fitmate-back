package com.fitmate.domain.mating.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GatherFee {
    RENTAL_FEE("대관료"),
    SNACK_FEE("다과비"),
    MATERIAL_FEE("재료비");

    private final String description;
}
