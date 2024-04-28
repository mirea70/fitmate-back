package com.fitmate.domain.mate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MateFee {

    private final MateFeeId id;
    private final Long mateId;
    private String name;
    private Integer fee;
    public static MateFee withOutId(Long mateId, String name, Integer fee) {
        return new MateFee(
                null,
                mateId,
                name,
                fee
        );
    }

    public static MateFee withId(MateFeeId id, Long mateId, String name, Integer fee) {
        return new MateFee(
                id,
                mateId,
                name,
                fee
        );
    }
}
