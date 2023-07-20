package com.fitmate.domain.mating.mate.domain.enums;

import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperateFee {
    HOST_FEE("호스트 수고비"),
    AVOID_NO_SHOW("노쇼 방지비"),
    PLATFORM_FEE("플랫폼 수수료");

    private final String description;

    public static OperateFee getValueFromDescription(String description) {
        return Arrays.stream(OperateFee.values())
                .filter(operateFee -> operateFee.description.equals(description)).findFirst()
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ENUM_DATA));
    }
}
