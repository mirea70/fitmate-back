package com.fitmate.domain.account;


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
}
