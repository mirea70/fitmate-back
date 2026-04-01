package com.fitmate.domain.mate.wish;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MateWish {

    private final MateWishId id;
    private final Long accountId;
    private final Long mateId;
    private final LocalDateTime createdAt;

    public static MateWish withId(MateWishId id, Long accountId, Long mateId, LocalDateTime createdAt) {
        return new MateWish(id, accountId, mateId, createdAt);
    }

    public static MateWish withoutId(Long accountId, Long mateId) {
        return new MateWish(null, accountId, mateId, null);
    }
}
