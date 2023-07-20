package com.fitmate.domain.mating.mate.repository;

import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.mate.helper.MatingDomainTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MatingDomainTestHelper.class)
@ActiveProfiles("rdbms")
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

    @Test
    public void 메이팅정보조회 () throws Exception {
        // given
        Mating savedMating = saveBefore();
        // when
        final Mating findResult = matingRepository.findById(savedMating.getId())
                .orElseThrow(Exception::new);
        // then
        assertThat(findResult).isNotNull();
        assertEquals(findResult, savedMating);
    }

    private Mating saveBefore() {
        Mating mating = matingDomainTestHelper.getTestMating();
        return matingRepository.save(mating);
    }

    @Test
    public void 메이트신청_대기리스트_추가 () throws Exception {
        // given
        Long accountId = 1L;
        Mating mating = saveBefore();
        int  beforeWaitingAccountCnt = mating.getWaitingAccountCnt();
        // when
        mating.addWaitingAccountId(accountId);
        Mating afterMating = matingRepository.save(mating);
        // then
        assertThat(afterMating).isNotNull();
        assertThat(afterMating.getWaitingAccountIds().contains(accountId)).isTrue();
        assertThat(afterMating.getWaitingAccountCnt()).isEqualTo(beforeWaitingAccountCnt + 1);
    }
}
