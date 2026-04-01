package com.fitmate.port.in.mate.usecase;

import com.fitmate.port.out.mate.dto.MateSimpleResponse;

import java.util.List;

public interface MateWishUseCasePort {
    boolean toggleWish(Long accountId, Long mateId);
    List<MateSimpleResponse> getMyWishMates(Long accountId);
}
