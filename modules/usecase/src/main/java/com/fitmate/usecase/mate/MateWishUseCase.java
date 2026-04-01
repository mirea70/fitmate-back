package com.fitmate.usecase.mate;

import com.fitmate.domain.mate.wish.MateWish;
import com.fitmate.port.in.mate.usecase.MateWishUseCasePort;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import com.fitmate.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional
public class MateWishUseCase implements MateWishUseCasePort {

    private final LoadMateWishPort loadMateWishPort;

    @Override
    public boolean toggleWish(Long accountId, Long mateId) {
        if (loadMateWishPort.existsWish(accountId, mateId)) {
            loadMateWishPort.deleteWish(accountId, mateId);
            return false;
        }
        MateWish mateWish = MateWish.withoutId(accountId, mateId);
        loadMateWishPort.saveWish(mateWish);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateSimpleResponse> getMyWishMates(Long accountId) {
        return loadMateWishPort.getWishedMates(accountId);
    }
}
