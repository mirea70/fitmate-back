package com.fitmate.port.in.mate.command;

import lombok.Getter;

@Getter
public class MateApplyCommand {
    private final Long mateId;
    private final Long applierId;
    private final String comeAnswer;

    public MateApplyCommand(Long mateId, Long applierId, String comeAnswer) {
        this.mateId = mateId;
        this.applierId = applierId;
        this.comeAnswer = comeAnswer;
    }
}
