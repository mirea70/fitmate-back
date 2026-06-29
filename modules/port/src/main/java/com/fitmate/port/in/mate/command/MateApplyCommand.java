package com.fitmate.port.in.mate.command;

public record MateApplyCommand(
        Long mateId,
        Long applierId,
        String comeAnswer
) {
}
