package com.fitmate.domain.mating.domain.enums;

import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum GatherFee {
    RENTAL_FEE("대관료"),
    SNACK_FEE("다과비"),
    MATERIAL_FEE("재료비");

    private final String description;

    public static GatherFee getValueFromDescription(String description) {
        return Arrays.stream(GatherFee.values())
                .filter(gatherFee -> gatherFee.description.equals(description)).findFirst()
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ENUM_DATA));
    }
}
