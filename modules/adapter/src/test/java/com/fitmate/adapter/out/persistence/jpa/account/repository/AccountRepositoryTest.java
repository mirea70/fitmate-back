package com.fitmate.adapter.out.persistence.jpa.account.repository;

import com.fitmate.adapter.out.persistence.jpa.BaseRepositoryTest;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountRepository 테스트")
class AccountRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private AccountJpaEntity savedAccount;

    @BeforeEach
    void setUp() {
        savedAccount = accountRepository.saveAndFlush(createAccountEntity("test"));
    }

    // @DataJpaTest의 @Transactional이 각 테스트 후 자동 롤백

    @Nested
    @DisplayName("checkDuplicated")
    class CheckDuplicatedTest {

        @Test
        @DisplayName("닉네임이 중복이면 true")
        void duplicateNickName() {
            boolean result = accountRepository.checkDuplicated(savedAccount.getId() + 1,
                    savedAccount.getNickName(), "other", "other@t.com", "01000000000");
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("이름이 중복이면 true")
        void duplicateName() {
            boolean result = accountRepository.checkDuplicated(savedAccount.getId() + 1,
                    "other", savedAccount.getName(), "other@t.com", "01000000000");
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("이메일이 중복이면 true")
        void duplicateEmail() {
            boolean result = accountRepository.checkDuplicated(savedAccount.getId() + 1,
                    "other", "other", savedAccount.getEmail(), "01000000000");
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("전화번호가 중복이면 true")
        void duplicatePhone() {
            boolean result = accountRepository.checkDuplicated(savedAccount.getId() + 1,
                    "other", "other", "other@t.com", savedAccount.getPhone());
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("아무것도 중복 아니면 false")
        void noDuplicate() {
            boolean result = accountRepository.checkDuplicated(savedAccount.getId() + 1,
                    "other", "other", "other@t.com", "01000000000");
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("본인 ID로 체크하면 항상 false")
        void selfCheckAlwaysFalse() {
            boolean result = accountRepository.checkDuplicated(savedAccount.getId(),
                    savedAccount.getNickName(), savedAccount.getName(), savedAccount.getEmail(), savedAccount.getPhone());
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("getById — 저장된 계정을 ID로 조회")
    void getById() {
        AccountJpaEntity found = accountRepository.getById(savedAccount.getId());
        assertThat(found.getLoginName()).isEqualTo(savedAccount.getLoginName());
    }

    @Test
    @DisplayName("getByLoginName — 로그인명으로 조회")
    void getByLoginName() {
        AccountJpaEntity found = accountRepository.getByLoginName(savedAccount.getLoginName());
        assertThat(found.getId()).isEqualTo(savedAccount.getId());
    }

    @Test
    @DisplayName("getByEmail — 이메일로 조회")
    void getByEmail() {
        AccountJpaEntity found = accountRepository.getByEmail(savedAccount.getEmail());
        assertThat(found.getId()).isEqualTo(savedAccount.getId());
    }

    @Test
    @DisplayName("existsByLoginName — 존재하면 true")
    void existsByLoginName_true() {
        assertThat(accountRepository.existsByLoginName(savedAccount.getLoginName())).isTrue();
    }

    @Test
    @DisplayName("existsByLoginName — 없으면 false")
    void existsByLoginName_false() {
        assertThat(accountRepository.existsByLoginName("nonexistent")).isFalse();
    }

    @Test
    @DisplayName("existsByPhone — 존재하면 true")
    void existsByPhone_true() {
        assertThat(accountRepository.existsByPhone(savedAccount.getPhone())).isTrue();
    }

    @Test
    @DisplayName("existsByPhone — 없으면 false")
    void existsByPhone_false() {
        assertThat(accountRepository.existsByPhone("01099999999")).isFalse();
    }

    @Test
    @DisplayName("findByPhone — 존재하면 Optional에 담겨 반환")
    void findByPhone() {
        Optional<AccountJpaEntity> found = accountRepository.findByPhone(savedAccount.getPhone());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedAccount.getId());
    }
}
