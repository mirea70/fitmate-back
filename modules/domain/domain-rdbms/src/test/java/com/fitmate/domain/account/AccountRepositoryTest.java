package com.fitmate.domain.account;


import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Account account = getTestAccount();
        // when
        final Account result = accountRepository.save(account);

        // then
        assertThat(result).isNotNull();
        assertEquals(result, account);
    }
    @Test
    public void 회원조회 () throws Exception {
        // given
        Account account = getTestAccount();
        accountRepository.save(account);
        // when
        final Account findResult = accountRepository.findByPrivateInfoEmail(account.getEmail());
        // then
        System.out.println("findResult.hashCode() = " + findResult.hashCode());
        System.out.println("account.hashCode() = " + account.hashCode());
        assertThat(findResult).isNotNull();
        assertEquals(findResult, account);
    }

    public Account getTestAccount() {

        final PrivateInfo privateInfo = PrivateInfo.builder()
                .name("홍길동")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();

        final ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("마이")
                .build();

        return Account.builder()
                .loginName("asd1")
                .password(Password.builder().value("12345678").build())
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .gender(Gender.MAIL)
                .role(AccountRole.USER)
                .build();
    }
}
