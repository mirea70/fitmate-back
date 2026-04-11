package com.fitmate.adapter.in.web.mate;

import com.fitmate.adapter.out.persistence.config.QueryDslConfig;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApprovedCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateApprovedCountRepository;
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
@DisplayName("메이트 신청 동시성 테스트 (낙관적 락 — 카운트 테이블 분리)")
class MateApplyConcurrencyTest {

    @Autowired
    private MateRepository mateRepository;

    @Autowired
    private MateApprovedCountRepository mateApprovedCountRepository;

    @Autowired
    private EntityManagerFactory emf;

    private MateJpaEntity createMate(int permitPeopleCnt) {
        return new MateJpaEntity(
                null, "FITNESS", "동시성 테스트", "소개",
                new HashSet<>(), LocalDateTime.now().plusDays(1),
                "장소", "주소", "FAST", "ALL",
                50, 15, permitPeopleCnt,
                100L, "질문", 0,
                null, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("MateApprovedCountJpaEntity에 @Version 필드가 존재하여 낙관적 락이 활성화되어 있음을 확인")
    void versionField_exists() {
        MateJpaEntity savedMate = mateRepository.saveAndFlush(createMate(3));
        MateApprovedCountJpaEntity savedCount = mateApprovedCountRepository.saveAndFlush(
                new MateApprovedCountJpaEntity(savedMate.getId())
        );

        assertThat(savedCount.getVersion()).isNotNull();
        System.out.println("=== @Version 필드 확인 (카운트 테이블) ===");
        System.out.println("version: " + savedCount.getVersion());
    }

    @Test
    @DisplayName("동시에 2명이 마지막 1자리에 신청하면 한 명만 성공 (카운트 테이블 낙관적 락 충돌)")
    void concurrentApply_onlyOneSucceeds() throws InterruptedException {
        // given: 정원 3명, 현재 승인 1명 (작성자)
        MateJpaEntity savedMate = mateRepository.saveAndFlush(createMate(3));
        Long mateId = savedMate.getId();
        MateApprovedCountJpaEntity countEntity = new MateApprovedCountJpaEntity(mateId);
        countEntity.increment(); // 작성자 1명
        mateApprovedCountRepository.saveAndFlush(countEntity);

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger conflictCount = new AtomicInteger(0);

        // when: 2명이 동시에 마지막 1자리에 신청 (카운트 테이블에 대해 경쟁)
        for (int i = 0; i < threadCount; i++) {
            long applierId = 200L + i;
            executor.submit(() -> {
                EntityManager em = emf.createEntityManager();
                try {
                    em.getTransaction().begin();
                    MateApprovedCountJpaEntity count = em.find(MateApprovedCountJpaEntity.class, mateId);

                    readyLatch.countDown();
                    startLatch.await();

                    count.increment();
                    em.getTransaction().commit();
                    successCount.incrementAndGet();
                    System.out.println("[성공] 유저 " + applierId + " 승인됨");
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

        // then: 카운트 테이블의 낙관적 락으로 한 명만 성공
        MateApprovedCountJpaEntity result = mateApprovedCountRepository.findById(mateId).orElseThrow();

        System.out.println("\n=== 테스트 결과 ===");
        System.out.println("성공: " + successCount.get());
        System.out.println("충돌(낙관적 락): " + conflictCount.get());
        System.out.println("최종 승인 인원: " + result.getApprovedCount());

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(1);
        assertThat(result.getApprovedCount()).isEqualTo(2); // 작성자 1 + 성공한 1
    }

    @Test
    @DisplayName("메이트 수정과 신청이 동시에 발생해도 서로 다른 @Version이므로 충돌하지 않음")
    void mateUpdateAndApply_noConflict() throws InterruptedException {
        // given: 메이트 + 카운트 레코드 생성
        MateJpaEntity savedMate = mateRepository.saveAndFlush(createMate(5));
        Long mateId = savedMate.getId();
        mateApprovedCountRepository.saveAndFlush(new MateApprovedCountJpaEntity(mateId));

        CountDownLatch readyLatch = new CountDownLatch(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);
        AtomicInteger successCount = new AtomicInteger(0);

        // when: 작성자가 글 수정 (mate 테이블) + 신청자가 신청 (count 테이블) 동시 수행
        // 글 수정 스레드
        new Thread(() -> {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                MateJpaEntity mate = em.find(MateJpaEntity.class, mateId);

                readyLatch.countDown();
                startLatch.await();

                mate.syncFrom(
                        mate.getFitCategory(), "수정된 제목", mate.getIntroduction(),
                        mate.getIntroImageIds(), mate.getMateAt(), mate.getFitPlaceName(),
                        mate.getFitPlaceAddress(), mate.getGatherType(), mate.getPermitGender(),
                        mate.getPermitMaxAge(), mate.getPermitMinAge(), mate.getPermitPeopleCnt(),
                        mate.getApplyQuestion(), mate.getTotalFee(), mate.getClosedAt()
                );
                em.getTransaction().commit();
                successCount.incrementAndGet();
                System.out.println("[성공] 글 수정 완료");
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                System.out.println("[충돌] 글 수정 실패 - " + e.getClass().getSimpleName());
            } finally {
                em.close();
                doneLatch.countDown();
            }
        }).start();

        // 신청 스레드
        new Thread(() -> {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                MateApprovedCountJpaEntity count = em.find(MateApprovedCountJpaEntity.class, mateId);

                readyLatch.countDown();
                startLatch.await();

                count.increment();
                em.getTransaction().commit();
                successCount.incrementAndGet();
                System.out.println("[성공] 신청 승인 완료");
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                System.out.println("[충돌] 신청 승인 실패 - " + e.getClass().getSimpleName());
            } finally {
                em.close();
                doneLatch.countDown();
            }
        }).start();

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();

        // then: 서로 다른 테이블이므로 둘 다 성공
        assertThat(successCount.get())
                .as("글 수정과 신청 승인이 서로 다른 @Version을 사용하므로 둘 다 성공해야 함")
                .isEqualTo(2);

        MateJpaEntity updatedMate = mateRepository.findById(mateId).orElseThrow();
        MateApprovedCountJpaEntity updatedCount = mateApprovedCountRepository.findById(mateId).orElseThrow();
        assertThat(updatedMate.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedCount.getApprovedCount()).isEqualTo(1);
    }
}
