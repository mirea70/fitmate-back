package com.fitmate.domain.account.service;


import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.helper.AccountDomainTestHelper;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.AccountErrorResult;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountService target;
    @Mock
    private AccountRepository accountRepository;

    private final AccountDomainTestHelper accountDomainTestHelper = new AccountDomainTestHelper();

    @Test
    public void 회원조회ID로_실패_존재X () throws Exception {
        // given
        Long accountId = 1L;
        doReturn(Optional.empty()).when(accountRepository).findById(anyLong());
        // when
        final NotFoundException result = assertThrows(NotFoundException.class,
                () -> target.validateFindById(accountId));
        // then
        assertThat(result.getErrorResult()).isEqualTo(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA);
    }

    @Test
    public void 회원조회ID로_성공 () throws Exception {
        // given
        Account account = accountDomainTestHelper.getTestAccount();
        Long accountId = 1L;
        doReturn(Optional.of(account)).when(accountRepository).findById(anyLong());
        // when
        final AccountDataDto.Response result = target.validateFindById(accountId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getPrivateInfo().getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    public void 중복체크_이메일로 () throws Exception {
        // given
        String email = "test@email.com";
        doReturn(Optional.of(Account.builder().build())).when(accountRepository).findByPrivateInfoEmail(anyString());
        // when
        final AccountDuplicatedException result = assertThrows(AccountDuplicatedException.class,
                () -> target.CheckDuplicatedByEmail(email));
        // then
        assertThat(result.getErrorResult()).isEqualTo(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);
    }

    @Test
    public void 중복체크_회원정보 () throws Exception {
        // given
        AccountDuplicateCheckDto checkDto = accountDomainTestHelper.getTestDuplicateCheckDto();
        doReturn(2).when(accountRepository).checkDuplicatedCount(anyString(), anyString(),
                                                                            anyString(), anyString());
        // when
        final AccountDuplicatedException result = assertThrows(AccountDuplicatedException.class,
                () -> target.CheckDuplicated(checkDto));
        // then
        assertThat(result.getErrorResult()).isEqualTo(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);
    }
}
