package com.fitmate.domain.account;


import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.enums.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void AccountRepository가Null이아님 () {
        assertThat(accountRepository).isNotNull();
    }
    @Test
    public void 회원등록 () throws Exception {
        // given
        final Account account = Account.builder()
                .name("홍길동")
                .loginName("asd1")
                .password("12345678")
                .email("abc@naver.com")
                .nickName("마이")
                .phone("01011112222")
                .role(AccountRole.USER)
                .build();
        // when
        final Account result = accountRepository.save(account);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
    }
    @Test
    public void 회원조회 () throws Exception {
        // given
        final Account account = Account.builder()
                .name("홍길동")
                .loginName("asd1")
                .password("12345678")
                .email("abc@naver.com")
                .nickName("마이")
                .phone("01011112222")
                .role(AccountRole.USER)
                .build();
        // when
        accountRepository.save(account);
        final Account findResult = accountRepository.findByEmail(account.getEmail());
        // then
        assertThat(findResult).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getPhone()).isEqualTo(account.getPhone());
    }
}
