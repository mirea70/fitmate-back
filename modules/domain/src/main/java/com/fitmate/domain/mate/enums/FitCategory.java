package com.fitmate.domain.mate.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FitCategory {
    FITNESS("헬스"),
    CROSSFIT("크로스핏"),
    RUNNING("러닝"),
    PILATES("필라테스"),
    YOGA("요가"),
    SWIMMING("수영"),
    BADMINTON("배드민턴"),
    TENNIS("테니스"),
    CLIMBING("클라이밍"),
    SOCCER("축구/풋살"),
    BASKETBALL("농구"),
    BASEBALL("야구"),
    CYCLING("자전거"),
    ETC("기타"),
    ;

    private final String name;
}
