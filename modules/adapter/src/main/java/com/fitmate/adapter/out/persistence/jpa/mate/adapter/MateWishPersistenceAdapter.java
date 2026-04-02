package com.fitmate.adapter.out.persistence.jpa.mate.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.mate.dto.MateSimpleJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateWishJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.mapper.MatePersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateWishRepository;
import com.fitmate.domain.mate.wish.MateWish;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.mate.dto.MateSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class MateWishPersistenceAdapter implements LoadMateWishPort {

    private final MateWishRepository mateWishRepository;
    private final MateQueryRepository mateQueryRepository;
    private final MatePersistenceMapper matePersistenceMapper;

    @Override
    public boolean existsWish(Long accountId, Long mateId) {
        return mateWishRepository.existsByAccountIdAndMateId(accountId, mateId);
    }

    @Override
    public void saveWish(MateWish mateWish) {
        MateWishJpaEntity entity = new MateWishJpaEntity(mateWish.getAccountId(), mateWish.getMateId());
        mateWishRepository.save(entity);
    }

    @Override
    public void deleteWish(Long accountId, Long mateId) {
        mateWishRepository.deleteByAccountIdAndMateId(accountId, mateId);
    }

    @Override
    public List<Long> getWisherAccountIds(Long mateId) {
        return mateWishRepository.findAllByMateId(mateId).stream()
                .map(MateWishJpaEntity::getAccountId)
                .toList();
    }

    @Override
    public List<MateSimpleResponse> getWishedMates(Long accountId) {
        List<MateSimpleJpaResponse> jpaResponses = mateQueryRepository.readListByWishAccountId(accountId);
        return matePersistenceMapper.jpaResponsesToResponses(jpaResponses);
    }
}
