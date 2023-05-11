package com.fitmate.domain.account.repository;


import com.fitmate.domain.account.entity.vo.PrivateInfo;
import com.fitmate.domain.account.helper.AccountDomainTestHelper;
import com.fitmate.domain.account.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(AccountDomainTestHelper.class)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountDomainTestHelper accountDomainTestHelper;

    @Test
    public void AccountRepository가Null이아님 () {
        assertThat(accountRepository).isNotNull();
    }
    @Test
    public void 회원등록실패_값중복 () throws Exception {
        // given
        saveBefore();
        Account newAccount = accountDomainTestHelper.getTestAccount();
        // when
        final DataIntegrityViolationException result = assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(newAccount));
        // then
        assertEquals(DataIntegrityViolationException.class, result.getClass());
    }

    private void saveBefore() {
        Account account = accountDomainTestHelper.getTestAccount();
        accountRepository.save(account);
    }
    @Test
    public void 회원등록 () throws Exception {
        // given
        Account account = accountDomainTestHelper.getTestAccount();
        // when
        final Account result = accountRepository.save(account);

        // then
        assertThat(result).isNotNull();
        assertEquals(result, account);
    }
    @Test
    public void 회원조회 () throws Exception {
        // given
        Account account = accountDomainTestHelper.getTestAccount();
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

    @Test
    public void 회원정보중복체크테스트 () throws Exception {
        // given
        Account originAccount = accountDomainTestHelper.getTestAccount();
        accountRepository.save(originAccount);

        PrivateInfo privateInfo = originAccount.getPrivateInfo();

        String name = privateInfo.getName();
        String email = privateInfo.getEmail();
        String phone = privateInfo.getPhone();
        String nickName = originAccount.getProfileInfo().getNickName();
        // when
        int againNameResult = accountRepository.checkDuplicatedCount(name,email + "2",
                phone + "2", nickName + "2");
        int againEmailResult = accountRepository.checkDuplicatedCount(name + "2",email,
                phone + "2", nickName + "2");
        int againPhoneResult = accountRepository.checkDuplicatedCount(name + "2",email + "2",
                phone, nickName + "2");
        int againNickNameResult = accountRepository.checkDuplicatedCount(name + "2",email + "2",
                phone + "2", nickName);
        // then
        assertThat(againNameResult).isGreaterThan(0);
        assertThat(againEmailResult).isGreaterThan(0);
        assertThat(againPhoneResult).isGreaterThan(0);
        assertThat(againNickNameResult).isGreaterThan(0);
    }

}
