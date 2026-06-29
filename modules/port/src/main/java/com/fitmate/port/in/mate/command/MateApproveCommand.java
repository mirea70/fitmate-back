package com.fitmate.port.in.mate.command;

public record MateApproveCommand(
        Long mateId,
        Long applierId,
        Long accountId
) {
}
