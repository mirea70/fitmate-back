package com.fitmate.domain.mating.request.repository;

import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import com.fitmate.domain.mating.request.helper.MateRequestDomainTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MateRequestDomainTestHelper.class)
@ActiveProfiles("rdbms")
@EnableJpaAuditing
public class MateRequestRepositoryTest {
    @Autowired
    private MateRequestRepository mateRequestRepository;
    @Autowired
    private MateRequestDomainTestHelper mateRequestDomainTestHelper;

    @Test
    public void 메이트신청_저장 () throws Exception {
        // given
        MateRequest mateRequest = mateRequestDomainTestHelper.getTestMateRequest();
        // when
        MateRequest result = mateRequestRepository.save(mateRequest);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreateAt()).isNotNull();
        assertThat(result).isEqualTo(mateRequest);
    }

    @Test
    public void 메이트신청_조회 () throws Exception {
        // given
        MateRequest savedRequest = saveBefore();
        // when
        final MateRequest findResult = mateRequestRepository.findById(savedRequest.getId())
                .orElseThrow(Exception::new);
        // then
        assertThat(findResult).isNotNull();
        assertEquals(findResult, savedRequest);
        assertThat(findResult.getId()).isEqualTo(savedRequest.getId());
    }

    private MateRequest saveBefore() {
        MateRequest mateRequest = mateRequestDomainTestHelper.getTestMateRequest();
        return mateRequestRepository.save(mateRequest);
    }
}
