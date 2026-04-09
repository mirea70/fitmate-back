package com.fitmate.domain.mate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PermitAges 값 객체 테스트")
class PermitAgesTest {

    @Test
    @DisplayName("정상 범위로 생성")
    void createValid() {
        PermitAges ages = new PermitAges(40, 20);
        assertThat(ages.getMax()).isEqualTo(40);
        assertThat(ages.getMin()).isEqualTo(20);
    }

    @Test
    @DisplayName("max가 50 초과하면 예외")
    void maxOver50() {
        assertThatThrownBy(() -> new PermitAges(51, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("50이하");
    }

    @Test
    @DisplayName("min이 15 미만이면 예외")
    void minUnder15() {
        assertThatThrownBy(() -> new PermitAges(40, 14))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("15이상");
    }

    @Test
    @DisplayName("update — 부분 업데이트")
    void update() {
        PermitAges ages = new PermitAges(40, 20);
        ages.update(45, null);
        assertThat(ages.getMax()).isEqualTo(45);
        assertThat(ages.getMin()).isEqualTo(20);
    }

    @Test
    @DisplayName("update — max가 50 초과하면 예외")
    void updateMaxOver50() {
        PermitAges ages = new PermitAges(40, 20);
        assertThatThrownBy(() -> ages.update(51, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
