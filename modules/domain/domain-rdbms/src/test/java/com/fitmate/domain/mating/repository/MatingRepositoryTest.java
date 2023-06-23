package com.fitmate.domain.mating.repository;

import com.fitmate.domain.mating.entity.Mating;
import com.fitmate.domain.mating.helper.MatingDomainTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MatingDomainTestHelper.class)
public class MatingRepositoryTest {
    @Autowired
    MatingRepository matingRepository;
    @Autowired
    MatingDomainTestHelper matingDomainTestHelper;

    @Test
    public void MatingRepository가Null이아님 () throws Exception {
        assertThat(matingRepository).isNotNull();
    }

    @Test
    public void 메이팅정보저장_참가비제외 () throws Exception {
        // given
        Mating mating = matingDomainTestHelper.getTestMatingNotEntryFeeInfo();
        // when
        Mating result = matingRepository.save(mating);
        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mating);
    }

    @Test
    public void 메이팅정보저장_참가비포함 () throws Exception {
        // given
        Mating mating = matingDomainTestHelper.getTestMating();
        // when
        Mating result = matingRepository.save(mating);
        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mating);
    }
}
