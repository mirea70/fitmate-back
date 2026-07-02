package com.fitmate.adapter.out.persistence.jpa.mate.repository;

import com.fitmate.adapter.out.persistence.jpa.BaseRepositoryTest;
import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateApplyJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MateApplyRepository 테스트")
class MateApplyRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private MateApplyRepository mateApplyRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUpActiveApplyUniqueIndex() {
        em.createNativeQuery("""
                ALTER TABLE mate_apply
                ADD COLUMN IF NOT EXISTS active_mate_id BIGINT
                GENERATED ALWAYS AS (
                    CASE WHEN deleted_at IS NULL THEN mate_id ELSE NULL END
                )
                """).executeUpdate();
        em.createNativeQuery("""
                ALTER TABLE mate_apply
                ADD COLUMN IF NOT EXISTS active_applier_id BIGINT
                GENERATED ALWAYS AS (
                    CASE WHEN deleted_at IS NULL THEN applier_id ELSE NULL END
                )
                """).executeUpdate();
        em.createNativeQuery("""
                CREATE UNIQUE INDEX IF NOT EXISTS uk_mate_apply_active
                ON mate_apply (active_mate_id, active_applier_id)
                """).executeUpdate();
    }

    @Test
    @DisplayName("같은 메이트에 같은 사용자의 활성 신청은 1건만 저장된다")
    void activeApplyUniqueConstraint() {
        mateApplyRepository.saveAndFlush(newApply(1L, 2L));

        assertThatThrownBy(() -> mateApplyRepository.saveAndFlush(newApply(1L, 2L)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("기존 신청을 취소하면 같은 메이트에 같은 사용자가 다시 신청할 수 있다")
    void cancelledApplyAllowsReapply() {
        MateApplyJpaEntity firstApply = mateApplyRepository.saveAndFlush(newApply(1L, 2L));
        firstApply.cancel("cancel");
        em.flush();
        em.clear();

        mateApplyRepository.saveAndFlush(newApply(1L, 2L));
    }

    private MateApplyJpaEntity newApply(Long mateId, Long applierId) {
        return new MateApplyJpaEntity(
                null,
                "answer",
                mateId,
                "WAIT",
                applierId,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
