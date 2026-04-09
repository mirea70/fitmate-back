package com.fitmate.adapter.out.persistence.jpa.follow.repository;

import com.fitmate.adapter.out.persistence.jpa.BaseRepositoryTest;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.adapter.out.persistence.jpa.follow.entity.FollowJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FollowRepository 테스트")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class FollowRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    private AccountJpaEntity accountA;
    private AccountJpaEntity accountB;

    @BeforeEach
    void setUp() {
        accountA = accountRepository.save(createAccountEntity("A"));
        accountB = accountRepository.save(createAccountEntity("B"));
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("팔로우 저장 및 조회")
    void saveAndFind() {
        AccountJpaEntity refA = em.find(AccountJpaEntity.class, accountA.getId());
        AccountJpaEntity refB = em.find(AccountJpaEntity.class, accountB.getId());
        FollowJpaEntity saved = followRepository.save(new FollowJpaEntity(refA, refB));
        em.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(followRepository.findById(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("deleteByFromAccountIdAndToAccountId — 특정 팔로우 삭제")
    void deleteByFromAndTo() {
        AccountJpaEntity refA = em.find(AccountJpaEntity.class, accountA.getId());
        AccountJpaEntity refB = em.find(AccountJpaEntity.class, accountB.getId());
        FollowJpaEntity saved = followRepository.save(new FollowJpaEntity(refA, refB));
        em.flush();
        em.clear();

        followRepository.deleteByFromAccountIdAndToAccountId(accountA.getId(), accountB.getId());
        em.flush();
        em.clear();

        assertThat(followRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("역방향 팔로우는 별도로 저장 가능")
    void reverseFollowAllowed() {
        AccountJpaEntity refA = em.find(AccountJpaEntity.class, accountA.getId());
        AccountJpaEntity refB = em.find(AccountJpaEntity.class, accountB.getId());
        FollowJpaEntity f1 = followRepository.save(new FollowJpaEntity(refA, refB));
        FollowJpaEntity f2 = followRepository.save(new FollowJpaEntity(refB, refA));
        em.flush();

        assertThat(f1.getId()).isNotNull();
        assertThat(f2.getId()).isNotNull();
        assertThat(f1.getId()).isNotEqualTo(f2.getId());
    }
}
