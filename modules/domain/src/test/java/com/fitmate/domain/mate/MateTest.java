package com.fitmate.domain.mate;

import com.fitmate.domain.error.exceptions.LimitException;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Mate 도메인 테스트")
class MateTest {

    private Mate mate;

    private Mate createMate(int approvedCount, int permitPeopleCnt) {
        return Mate.withId(
                new MateId(1L),
                FitCategory.FITNESS,
                "운동 메이트 구합니다",
                "같이 운동해요",
                Set.of(1L),
                LocalDateTime.now().plusDays(7),
                new FitPlace("험블짐", "서울시 용산구"),
                GatherType.AGREE,
                PermitGender.ALL,
                new PermitAges(40, 20),
                permitPeopleCnt,
                100L,
                List.of(MateFee.withOutId(1L, "장소 대여비", 10000)),
                "어떤 운동 좋아하세요?",
                approvedCount,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @BeforeEach
    void setUp() {
        mate = createMate(0, 5);
    }

    @Nested
    @DisplayName("승인 카운트")
    class ApprovedCount {

        @Test
        @DisplayName("승인 카운트 증가")
        void increment() {
            mate.incrementApprovedCount();
            assertThat(mate.getApprovedCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("승인 카운트 감소")
        void decrement() {
            mate.incrementApprovedCount();
            mate.decrementApprovedCount();
            assertThat(mate.getApprovedCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("승인 카운트가 0이면 감소해도 0 유지")
        void decrementAtZero() {
            mate.decrementApprovedCount();
            assertThat(mate.getApprovedCount()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("정원 체크")
    class Capacity {

        @Test
        @DisplayName("isFull — (approvedCount + 1) >= permitPeopleCnt이면 true")
        void isFull() {
            // permitPeopleCnt=5, 작성자(+1) 포함이므로 approvedCount=4이면 full
            Mate fullMate = createMate(4, 5);
            assertThat(fullMate.isFull()).isTrue();
        }

        @Test
        @DisplayName("isFull — 여유 있으면 false")
        void isNotFull() {
            assertThat(mate.isFull()).isFalse();
        }

        @Test
        @DisplayName("정원 초과 시 incrementApprovedCount에서 LimitException 발생")
        void checkCapacityThrows() {
            Mate fullMate = createMate(4, 5);
            assertThatThrownBy(fullMate::incrementApprovedCount)
                    .isInstanceOf(LimitException.class);
        }

        @Test
        @DisplayName("정원 미달 시 checkCapacity 통과")
        void checkCapacityPasses() {
            mate.checkCapacity(); // no exception
        }
    }

    @Nested
    @DisplayName("마감")
    class Close {

        @Test
        @DisplayName("마감 처리 시 closedAt이 설정됨")
        void close() {
            mate.close();
            assertThat(mate.isClosed()).isTrue();
            assertThat(mate.getClosedAt()).isNotNull();
        }

        @Test
        @DisplayName("이미 마감된 메이트는 다시 마감해도 closedAt 변경 안됨")
        void closeIdempotent() {
            mate.close();
            LocalDateTime first = mate.getClosedAt();
            mate.close();
            assertThat(mate.getClosedAt()).isEqualTo(first);
        }
    }

    @Test
    @DisplayName("withoutId로 생성하면 approvedCount가 0")
    void withoutId() {
        Mate newMate = Mate.withoutId(
                FitCategory.RUNNING, "제목", "소개", null,
                LocalDateTime.now().plusDays(1),
                new FitPlace("장소", "주소"),
                GatherType.FAST, PermitGender.ALL,
                new PermitAges(50, 15), 5, 1L,
                Collections.emptyList(), "질문"
        );
        assertThat(newMate.getApprovedCount()).isEqualTo(0);
        assertThat(newMate.getId()).isNull();
    }

    @Test
    @DisplayName("totalFee는 mateFees의 합산")
    void totalFee() {
        Mate m = Mate.withoutId(
                FitCategory.FITNESS, "제목", "소개", null,
                LocalDateTime.now().plusDays(1),
                new FitPlace("장소", "주소"),
                GatherType.AGREE, PermitGender.ALL,
                new PermitAges(50, 15), 5, 1L,
                List.of(
                        MateFee.withOutId(null, "비용1", 5000),
                        MateFee.withOutId(null, "비용2", 3000)
                ), "질문"
        );
        assertThat(m.getTotalFee()).isEqualTo(8000);
    }

    @Test
    @DisplayName("update로 부분 수정 시 null 필드는 기존 값 유지")
    void update() {
        mate.update(
                FitCategory.YOGA, null, null, null,
                null, null, null,
                null, null, null, null, null,
                null, null
        );
        assertThat(mate.getFitCategory()).isEqualTo(FitCategory.YOGA);
        assertThat(mate.getTitle()).isEqualTo("운동 메이트 구합니다");
    }

    @Test
    @DisplayName("updateIntroImages — null 또는 빈 세트이면 예외")
    void updateIntroImagesThrows() {
        assertThatThrownBy(() -> mate.updateIntroImages(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mate.updateIntroImages(Set.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("updateIntroImages — 정상 세트이면 교체")
    void updateIntroImages() {
        mate.updateIntroImages(Set.of(10L, 20L));
        assertThat(mate.getIntroImageIds()).containsExactlyInAnyOrder(10L, 20L);
    }
}
