package com.fitmate.app.account.service;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.account.service.JoinService;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JoinServiceTest {
    @InjectMocks
    private JoinService target;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountService accountService;
    private AccountAppTestHelper accountAppTestHelper = new AccountAppTestHelper();

    @Test
    public void 회원가입실패_값중복 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();

        doThrow(DataIntegrityViolationException.class).when(accountRepository).save(any());
        // when
        final AccountDuplicatedException result = assertThrows(AccountDuplicatedException.class,
                () -> target.join(joinRequest));
        // then
        assertEquals(AccountDuplicatedException.class, result.getClass());
        assertThat(result.getErrorResult()).isEqualTo(AccountErrorResult.DUPLICATED_ACCOUNT_VALUE);
    }

    @Test
    public void 회원가입성공 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        Account account = AccountDtoMapper.INSTANCE.toEntity(joinRequest);
        doReturn(account).when(accountRepository).save(any(Account.class));
        // when
        final AccountDto.JoinResponse result = target.join(joinRequest);
        // then
        assertThat(result).isNotNull();
        assertEquals(result.getPrivateInfo().getEmail(), joinRequest.getPrivateInfo().getEmail());

        verify(accountService, times(1)).CheckDuplicated(any(AccountDuplicateCheckDto.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}