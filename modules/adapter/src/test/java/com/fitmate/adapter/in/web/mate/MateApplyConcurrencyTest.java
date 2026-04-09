package com.fitmate.adapter.in.web.mate;

import com.fitmate.adapter.out.persistence.config.QueryDslConfig;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("메이트 신청 동시성 테스트 (낙관적 락)")
class MateApplyConcurrencyTest {

    @Autowired
    private MateRepository mateRepository;

    @Autowired
    private EntityManagerFactory emf;

    private MateJpaEntity createMate(int permitPeopleCnt, int approvedCount) {
        return new MateJpaEntity(
                null, "FITNESS", "동시성 테스트", "소개",
                new HashSet<>(), LocalDateTime.now().plusDays(1),
                "장소", "주소", "FAST", "ALL",
                50, 15, permitPeopleCnt,
                100L, "질문", 0,
                approvedCount,
                null, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("@Version 필드가 존재하여 낙관적 락이 활성화되어 있음을 확인")
    void versionField_exists() {
        MateJpaEntity saved = mateRepository.saveAndFlush(createMate(3, 1));

        assertThat(saved.getVersion()).isNotNull();
        System.out.println("=== @Version 필드 확인 ===");
        System.out.println("version: " + saved.getVersion());
    }

    @Test
    @DisplayName("동시에 2명이 마지막 1자리에 신청하면 한 명만 성공 (낙관적 락 충돌)")
    void concurrentApply_onlyOneSucceeds() throws InterruptedException {
        // given: 정원 2명, 현재 1명(작성자) 승인
        MateJpaEntity saved = mateRepository.saveAndFlush(createMate(2, 1));
        Long mateId = saved.getId();

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);

        // when: 2명이 동시에 마지막 1자리에 신청
        for (int i = 0; i < threadCount; i++) {
            long applierId = 200L + i;
            executor.submit(() -> {
                EntityManager em = emf.createEntityManager();
                try {
                    em.getTransaction().begin();
                    MateJpaEntity mate = em.find(MateJpaEntity.class, mateId);

                    readyLatch.countDown();
                    startLatch.await();

                    if (mate.getApprovedCount() < mate.getPermitPeopleCnt()) {
                        mate.syncFrom(
                                mate.getFitCategory(), mate.getTitle(), mate.getIntroduction(),
                                mate.getIntroImageIds(), mate.getMateAt(), mate.getFitPlaceName(),
                                mate.getFitPlaceAddress(), mate.getGatherType(), mate.getPermitGender(),
                                mate.getPermitMaxAge(), mate.getPermitMinAge(), mate.getPermitPeopleCnt(),
                                mate.getApplyQuestion(), mate.getTotalFee(),
                                mate.getApprovedCount() + 1, mate.getClosedAt()
                        );
                        em.getTransaction().commit();
                        successCount.incrementAndGet();
                        System.out.println("[성공] 유저 " + applierId + " 승인됨");
                    }
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    conflictCount.incrementAndGet();
                    System.out.println("[충돌] 유저 " + applierId + " - " + e.getClass().getSimpleName());
                } finally {
                    em.close();
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        // then
        MateJpaEntity result = mateRepository.findById(mateId).orElseThrow();

        System.out.println("\n=== 테스트 결과 ===");
        System.out.println("성공: " + successCount.get());
        System.out.println("충돌(낙관적 락): " + conflictCount.get());
        System.out.println("최종 승인 인원: " + result.getApprovedCount());
        System.out.println("정원: " + result.getPermitPeopleCnt());

        assertThat(result.getApprovedCount())
                .as("승인 인원이 정원(%d)을 초과하면 안 됨", result.getPermitPeopleCnt())
                .isLessThanOrEqualTo(result.getPermitPeopleCnt());

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(1);
    }
}
