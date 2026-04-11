package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.BaseRepositoryTest;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.domain.error.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MateRepository 테스트")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class MateRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private MateRepository mateRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    private Long writerId;
    private MateJpaEntity savedMate;

    @BeforeEach
    void setUp() {
        AccountJpaEntity writer = accountRepository.save(createAccountEntity("writer"));
        em.flush();
        writerId = writer.getId();

        savedMate = mateRepository.save(new MateJpaEntity(
                null, "FITNESS", "운동 메이트 구함", "같이 해요",
                new HashSet<>(),
                LocalDateTime.now().plusDays(3),
                "험블짐", "서울시 용산구", "AGREE", "ALL",
                50, 15, 5, writerId,
                "어떤 운동?", 10000, null,
                LocalDateTime.now(), LocalDateTime.now()
        ));
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("getById — 존재하는 ID로 조회")
    void getById() {
        MateJpaEntity found = mateRepository.getById(savedMate.getId());
        assertThat(found.getTitle()).isEqualTo("운동 메이트 구함");
    }

    @Test
    @DisplayName("getById — 존재하지 않는 ID로 조회하면 NotFoundException")
    void getByIdNotFound() {
        assertThatThrownBy(() -> mateRepository.getById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("findAllByMateAtBetween — 날짜 범위 내 메이트 조회")
    void findByMateAtBetween() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(14);

        List<MateJpaEntity> result = mateRepository.findAllByMateAtBetween(from, to);
        assertThat(result).anyMatch(m -> m.getId().equals(savedMate.getId()));
    }

    @Test
    @DisplayName("findAllByMateAtBetween — 범위 밖이면 해당 메이트 미포함")
    void findByMateAtBetween_empty() {
        LocalDateTime from = LocalDateTime.now().plusDays(10);
        LocalDateTime to = LocalDateTime.now().plusDays(20);

        List<MateJpaEntity> result = mateRepository.findAllByMateAtBetween(from, to);
        assertThat(result).noneMatch(m -> m.getId().equals(savedMate.getId()));
    }

    @Test
    @DisplayName("findAllByMateAtBeforeAndClosedAtIsNull — 미래 메이트는 미포함")
    void findUnclosedPastMates() {
        List<MateJpaEntity> result = mateRepository.findAllByMateAtBeforeAndClosedAtIsNull(LocalDateTime.now());
        assertThat(result).noneMatch(m -> m.getId().equals(savedMate.getId()));
    }

    @Test
    @DisplayName("version 필드가 설정됨")
    void versionExists() {
        MateJpaEntity found = mateRepository.getById(savedMate.getId());
        assertThat(found.getVersion()).isNotNull();
    }
}
