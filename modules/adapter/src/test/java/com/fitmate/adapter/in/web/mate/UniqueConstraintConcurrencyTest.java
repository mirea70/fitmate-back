package com.fitmate.adapter.in.web.mate;

import com.fitmate.adapter.out.persistence.config.QueryDslConfig;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import com.fitmate.adapter.out.persistence.jpa.follow.entity.FollowJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.follow.repository.FollowRepository;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateWishJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateWishRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("유니크 제약 동시성 테스트")
class UniqueConstraintConcurrencyTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MateWishRepository mateWishRepository;

    @Autowired
    private EntityManagerFactory emf;

    private Long accountAId;
    private Long accountBId;

    private static int counter = 0;

    @BeforeEach
    void setUp() {
        counter++;
        AccountJpaEntity accountA = AccountJpaEntity.builder()
                .loginName("testA" + counter)
                .password("pass")
                .nickName("유저A" + counter)
                .name("테스터A" + counter)
                .phone("0101111" + String.format("%04d", counter))
                .email("a" + counter + "@test.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .gender("MALE")
                .role("USER")
                .build();

        AccountJpaEntity accountB = AccountJpaEntity.builder()
                .loginName("testB" + counter)
                .password("pass")
                .nickName("유저B" + counter)
                .name("테스터B" + counter)
                .phone("0102222" + String.format("%04d", counter))
                .email("b" + counter + "@test.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .gender("FEMALE")
                .role("USER")
                .build();

        accountAId = accountRepository.saveAndFlush(accountA).getId();
        accountBId = accountRepository.saveAndFlush(accountB).getId();
    }

    @Test
    @DisplayName("팔로우 — 동시에 같은 대상을 팔로우하면 유니크 제약으로 1건만 저장")
    void follow_concurrent_onlyOneSaved() throws InterruptedException {
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                EntityManager em = emf.createEntityManager();
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    em.getTransaction().begin();
                    AccountJpaEntity fromAccount = em.find(AccountJpaEntity.class, accountAId);
                    AccountJpaEntity toAccount = em.find(AccountJpaEntity.class, accountBId);
                    FollowJpaEntity follow = new FollowJpaEntity(fromAccount, toAccount);
                    em.persist(follow);
                    em.getTransaction().commit();
                    successCount.incrementAndGet();
                    System.out.println("[성공] 팔로우 저장됨");
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    failCount.incrementAndGet();
                    System.out.println("[실패] 유니크 제약 위반 - " + e.getClass().getSimpleName());
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

        long followCount = followRepository.count();

        System.out.println("\n=== 팔로우 동시성 테스트 결과 ===");
        System.out.println("성공: " + successCount.get());
        System.out.println("실패(유니크 위반): " + failCount.get());
        System.out.println("DB 팔로우 수: " + followCount);

        assertThat(followCount)
                .as("동시 요청해도 팔로우는 1건만 저장되어야 함")
                .isEqualTo(1);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("찜 — 동시에 같은 메이트를 찜하면 유니크 제약으로 1건만 저장")
    void wish_concurrent_onlyOneSaved() throws InterruptedException {
        Long fakeMateId = 999L;

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                EntityManager em = emf.createEntityManager();
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    em.getTransaction().begin();
                    MateWishJpaEntity wish = new MateWishJpaEntity(accountAId, fakeMateId);
                    em.persist(wish);
                    em.getTransaction().commit();
                    successCount.incrementAndGet();
                    System.out.println("[성공] 찜 저장됨");
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                    failCount.incrementAndGet();
                    System.out.println("[실패] 유니크 제약 위반 - " + e.getClass().getSimpleName());
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

        long wishCount = mateWishRepository.count();

        System.out.println("\n=== 찜 동시성 테스트 결과 ===");
        System.out.println("성공: " + successCount.get());
        System.out.println("실패(유니크 위반): " + failCount.get());
        System.out.println("DB 찜 수: " + wishCount);

        assertThat(wishCount)
                .as("동시 요청해도 찜은 1건만 저장되어야 함")
                .isEqualTo(1);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(1);
    }
}
