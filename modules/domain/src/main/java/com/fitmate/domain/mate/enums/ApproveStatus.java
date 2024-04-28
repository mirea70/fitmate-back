package com.fitmate.domain.mate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApproveStatus {
    WAIT("대기중"),
    APPROVE("승인");

    private final String label;
}
