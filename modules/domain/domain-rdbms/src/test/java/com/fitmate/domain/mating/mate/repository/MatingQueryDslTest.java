package com.fitmate.domain.mating.mate.repository;

import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.helper.AccountDomainTestHelper;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.mate.dto.MatingQuestionDto;
import com.fitmate.domain.mating.mate.dto.MatingReadResponseDto;
import com.fitmate.domain.mating.mate.dto.QMatingQuestionDto_Response;
import com.fitmate.domain.mating.mate.dto.QMatingReadResponseDto;
import com.fitmate.domain.mating.mate.helper.MatingDomainTestHelper;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fitmate.domain.account.entity.QAccount.account;
import static com.fitmate.domain.mating.mate.domain.entity.QMating.mating;
import static com.fitmate.domain.mating.request.domain.entity.QMateRequest.mateRequest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"dev","rdbms"})
public class MatingQueryDslTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private MatingRepository matingRepository;
    @Autowired
    private MateRequestRepository mateRequestRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MatingDomainTestHelper matingDomainTestHelper;
    @Autowired
    private AccountDomainTestHelper accountDomainTestHelper;

    @Autowired
    private Environment env;
    @Autowired
    private EntityManager em;

    @Test
    public void testGetDataSourceUrl() {
        assertThat(env.getProperty("spring.datasource.url")).isEqualTo("jdbc:oracle:thin:@fitmatedev_high?TNS_ADMIN=/Users/growme/oci/wallet/Wallet_fitmateDev");
    }

    @Transactional
    public Mating saveMatingBefore() {
        Mating mating = matingDomainTestHelper.getTestMating();
        return matingRepository.save(mating);
    }
    @Transactional
    public Account saveAccountBefore() {
        Account account = accountDomainTestHelper.getTestAccount();
        return accountRepository.save(account);
    }


    @Transactional
    public MateRequest saveMateRequestBefore() {
        MateRequest mateRequest = matingDomainTestHelper.getTestMateRequest();
        return mateRequestRepository.save(mateRequest);
    }

    @Transactional
    public void deleteMatingAfter() {
        matingRepository.deleteAll();
    }

    @Transactional
    public void deleteMateRequestAfter() {
        mateRequestRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 메이팅글_목록조회_테스트() throws Exception {
        // given
        Long lastMatingId = saveMatingBefore().getId();
        int limit = 10;
        // when
        List<MatingReadResponseDto> responses = jpaQueryFactory
                .select(new QMatingReadResponseDto(mating.id, mating.fitCategory, mating.title,
                        mating.mateAt, mating.fitPlace.name, mating.fitPlace.address, mating.gatherType,
                        mating.permitGender, mating.permitAges.max, mating.permitAges.min, mating.permitPeopleCnt,
                        mating.waitingAccountCnt, mating.approvedAccountCnt))
                .from(mating)
                .orderBy(mating.createdAt.desc())
                .where(afterLastMatingId(lastMatingId-1))
                .limit(limit)
                .fetch();
        // then
        assertThat(responses.size()).isNotEqualTo(0);
        assertThat(responses.get(responses.size() - 1).getId()).isEqualTo(lastMatingId);
    }

    private BooleanExpression afterLastMatingId(Long lastMatingId) {
        return lastMatingId != null ? mating.id.gt(lastMatingId) : null;
    }

    @Test
    @Transactional
    public void 메이팅신청_질문화면_조회_테스트 () throws Exception {
        // given : 한 사용자가 메이팅 글 등록
        Account initAccount = saveAccountBefore();
        Mating initMating = saveMatingBefore();
        initMating.updateWriterId(initAccount.getId());

        Long matingId = initMating.getId();
        // when
        MatingQuestionDto.Response response = jpaQueryFactory
                .select(new QMatingQuestionDto_Response(account.profileInfo.profileImageId, account.profileInfo.nickName, mating.comeQuestion))
                .from(mating)
                .where(mating.id.eq(matingId))
                .innerJoin(account).on(mating.writerId.eq(account.id))
                .fetchOne();
        // then
        assertThat(response).isNotNull();
        assertThat(response.getComeQuestion()).isEqualTo(initMating.getComeQuestion());
    }

    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void 메이트신청_승인상태_변경_테스트 () throws Exception {
        // given
        MateRequest testMateRequest = saveMateRequestBefore();

        Long matingId = testMateRequest.getMatingId();
        Set<Long> accountIds = new HashSet<>();
        accountIds.add(testMateRequest.getAccountId());
        // when
        jpaQueryFactory
                .update(mateRequest)
                .set(mateRequest.approveStatus, MateRequest.ApproveStatus.APPROVE)
                .where(mateRequest.matingId.eq(matingId), mateRequest.accountId.in(accountIds))
                .execute();
        em.flush();
        em.clear();

        List<MateRequest> afterMateRequests = mateRequestRepository.findAllByMatingIdAndAccountIdIn(matingId, accountIds);
        // then
        afterMateRequests.forEach(afterMateRequest -> {
            assertThat(afterMateRequest.getApproveStatus()).isEqualTo(MateRequest.ApproveStatus.APPROVE);
        });
    }
}
