package com.fitmate.domain.account;


import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(AccountTestHelper.class)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountTestHelper accountTestHelper;

    @Test
    public void AccountRepository가Null이아님 () {
        assertThat(accountRepository).isNotNull();
    }
    @Test
    public void 회원등록실패_값중복 () throws Exception {
        // given
        saveBefore();
        Account newAccount = accountTestHelper.getTestAccount();
        // when
        final DataIntegrityViolationException result = assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(newAccount));
        // then
        assertEquals(DataIntegrityViolationException.class, result.getClass());
    }

    private void saveBefore() {
        Account account = accountTestHelper.getTestAccount();
        accountRepository.save(account);
    }
    @Test
    public void 회원등록 () throws Exception {
        // given
        Account account = accountTestHelper.getTestAccount();
        // when
        final Account result = accountRepository.save(account);

        // then
        assertThat(result).isNotNull();
        assertEquals(result, account);
    }
    @Test
    public void 회원조회 () throws Exception {
        // given
        Account account = accountTestHelper.getTestAccount();
        accountRepository.save(account);
        // when
        final Account findResult = accountRepository.findByPrivateInfoEmail(account.getEmail())
                .orElseThrow(Exception::new);
        // then
        System.out.println("findResult.hashCode() = " + findResult.hashCode());
        System.out.println("account.hashCode() = " + account.hashCode());
        assertThat(findResult).isNotNull();
        assertEquals(findResult, account);
    }

}
