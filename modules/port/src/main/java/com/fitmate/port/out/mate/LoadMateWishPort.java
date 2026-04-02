package com.fitmate.port.out.mate;

import com.fitmate.domain.mate.wish.MateWish;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;

import java.util.List;

public interface LoadMateWishPort {
    boolean existsWish(Long accountId, Long mateId);
    void saveWish(MateWish mateWish);
    void deleteWish(Long accountId, Long mateId);
    List<MateSimpleResponse> getWishedMates(Long accountId);
    List<Long> getWisherAccountIds(Long mateId);
}
