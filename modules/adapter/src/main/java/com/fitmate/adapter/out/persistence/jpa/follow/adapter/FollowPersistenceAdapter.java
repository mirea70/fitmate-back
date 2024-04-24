package com.fitmate.adapter.out.persistence.jpa.follow.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.follow.dto.FollowDetailJpaResponse;
import com.fitmate.adapter.out.persistence.jpa.follow.entity.FollowJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.follow.mapper.FollowPersistenceMapper;
import com.fitmate.adapter.out.persistence.jpa.follow.repository.FollowQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.follow.repository.FollowRepository;
import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.port.out.follow.FollowDetailResponse;
import com.fitmate.port.out.follow.LoadFollowPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class FollowPersistenceAdapter implements LoadFollowPort {

    private final FollowRepository followRepository;
    private final FollowQueryRepository followQueryRepository;
    private final FollowPersistenceMapper followPersistenceMapper;

    @Override
    public void saveFollowEntity(Long fromAccountId, Long toAccountId) {
        FollowJpaEntity followEntity = new FollowJpaEntity(fromAccountId, toAccountId);
        followRepository.save(followEntity);
    }

    @Override
    public void deleteFollowEntity(Long fromAccountId, Long toAccountId) {
        followRepository.deleteByFromAccountIdAndToAccountId(fromAccountId, toAccountId);
    }

    @Override
    public void deleteAllFollowByAccountId(AccountId id) {
        followRepository.deleteAllByFromAccountIdOrToAccountId(id.getValue(), id.getValue());
    }

    @Override
    public List<FollowDetailResponse> getFollowingsByFrom(Long fromAccountId) {
        List<FollowDetailJpaResponse> jpaResponses = followQueryRepository.getFollowingsByFrom(fromAccountId);
        return followPersistenceMapper.jpaResponsesToResponses(jpaResponses);
    }

    @Override
    public List<FollowDetailResponse> getFollowersByTarget(Long targetAccountId) {
        List<FollowDetailJpaResponse> jpaResponses = followQueryRepository.getFollowersByTarget(targetAccountId);
        return followPersistenceMapper.jpaResponsesToResponses(jpaResponses);
    }
}
