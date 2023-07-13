package com.fitmate.domain.mating.repository;

import com.fitmate.domain.config.QueryDslConfig;
import com.fitmate.domain.mating.domain.entity.Mating;
import com.fitmate.domain.mating.domain.repository.MatingRepository;
import com.fitmate.domain.mating.helper.MatingDomainTestHelper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.fitmate.domain.mating.domain.entity.QMating.mating;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import({QueryDslConfig.class, MatingDomainTestHelper.class})
@ActiveProfiles("domain")
public class MatingQueryDslTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private MatingRepository matingRepository;
    @Autowired
    private MatingDomainTestHelper matingDomainTestHelper;

    @Autowired
    private Environment env;

    @Test
    public void testGetDataSourceUrl() {
        assertThat(env.getProperty("spring.datasource.url")).isEqualTo("jdbc:mysql://localhost:18000/fitmate?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8");
    }

//    @BeforeEach
//    @Transactional
//    public void saveBefore() {
//        Mating mating = matingDomainTestHelper.getTestMating();
//        matingRepository.save(mating);
//    }

    @Test
    @Transactional
    public void 메이팅글_목록조회_테스트() throws Exception {
        // given
        Long lastMatingId = 6L;
        int limit = 10;
        // when
        List<Mating> responses = jpaQueryFactory
                .select(mating)
                .from(mating)
                .orderBy(mating.createdAt.desc())
                .where(afterLastMatingId(lastMatingId))
                .limit(limit)
                .fetch();
        // then
        assertThat(responses.size()).isNotEqualTo(0);
        assertThat(responses.get(responses.size() - 1).getId()).isEqualTo(7L);
    }

    private BooleanExpression afterLastMatingId(Long lastMatingId) {
        return lastMatingId != null ? mating.id.gt(lastMatingId) : null;
    }
}
