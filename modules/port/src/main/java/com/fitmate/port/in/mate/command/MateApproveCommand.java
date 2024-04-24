package com.fitmate.port.in.mate.command;

import lombok.Getter;

@Getter
public class MateApproveCommand {
    private final Long mateId;
    private final Long applierId;
    private final Long accountId;

    public MateApproveCommand(Long mateId, Long applierId, Long accountId) {
        this.mateId = mateId;
        this.applierId = applierId;
        this.accountId = accountId;
    }
}
