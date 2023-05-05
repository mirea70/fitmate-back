package com.fitmate.account.service;

import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.AccountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    private AccountService target;
    @Mock
    private AccountRepository accountRepository;

    private final String email = "abc@naver.com";

    @Test
    public void 회원가입실패_이미존재함 () throws Exception {
        // given
        doReturn(Account.builder().build()).when(accountRepository).findByEmail(email);

        // when
        final AccountException result = assertThrows(AccountException.class,
                () -> target.join(email));
        // then
        assertThat(result.getErrorResult()).isEqulTo();
    }
}