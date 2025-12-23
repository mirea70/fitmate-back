package com.fitmate.adapter.out.persistence.jpa.account.repository;

import com.fitmate.adapter.out.persistence.config.QueryDslConfig;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Nested
    @DisplayName("checkDuplicated 함수 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CheckDuplicatedTest {

        static Long alreadyId;
        static String alreadyNickName = "alreadyNickName";
        static String alreadyName = "aName";
        static String alreadyEmail = "alreadyEmail";
        static String alreadyPhone = "01011111111";

        @BeforeAll
        void setUp() {
            alreadyId = saveAccount(
                    alreadyNickName,
                    alreadyName,
                    alreadyEmail,
                    alreadyPhone
            );
        }

        @DisplayName("주요 회원 정보들 중, 자신을 제외하고 하나라도 중복된 것이 있는지 체크한다.")
        @ParameterizedTest
        @MethodSource("provideAccountInfoForCheckDuplicated")
        void checkDuplicated(String nickName, String name, String email, String phone, boolean expected) {
            // when
            boolean result1 = accountRepository.checkDuplicated(alreadyId + 1, nickName, name, email, phone);
            boolean result2 = accountRepository.checkDuplicated(alreadyId, nickName, name, email, phone);
            // then
            assertThat(result1).isEqualTo(expected);
            assertThat(result2).isEqualTo(false);
        }

        private static Stream<Arguments> provideAccountInfoForCheckDuplicated() {
            return Stream.of(
                    Arguments.of(alreadyNickName, "name", "email", "01012345678", true),
                    Arguments.of("nickName", alreadyName, "email", "01012345678", true),
                    Arguments.of("nickName", "name", alreadyEmail, "01012345678", true),
                    Arguments.of("nickName", "name", "email", alreadyPhone, true),
                    Arguments.of("nickName", "name", "email", "01012345678", false)
            );
        }

        private Long saveAccount(String nickName, String name, String email, String phone) {
            return accountRepository.save(
                    AccountJpaEntity.builder()
                            .loginName("loginName")
                            .password("@Aa111111")
                            .nickName(nickName)
                            .introduction("intro")
                            .name(name)
                            .email(email)
                            .phone(phone)
                            .gender("MALE")
                            .role("ROLE_USER")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            ).getId();
        }
    }

    @Test
    void getById() {
    }

    @Test
    void getByEmail() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void getByLoginName() {
    }

    @Test
    void findByLoginName() {
    }

    @Test
    void existsByLoginName() {
    }

    @Test
    void existsByPhone() {
    }
}