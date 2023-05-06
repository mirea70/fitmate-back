package com.fitmate.account.service;

import com.fitmate.account.dto.AccountDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
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

    @Test
    public void 회원가입실패_이미존재함 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest =
                AccountDto.JoinRequest.builder()
                        .name("미이수")
                        .loginName("abc2")
                        .password("1234")
                        .nickName("닉네임2")
                        .phone("12345678")
                        .email("abc@naver.com")
                        .role(AccountRole.USER)
                        .build();

        doReturn(Account.builder().build()).when(accountRepository).findByPrivateInfoEmail(joinRequest.getEmail());
        // when
        final AccountDuplicatedException result = assertThrows(AccountDuplicatedException.class,
                () -> target.join(joinRequest));
        // then
        assertThat(result.getErrorResult()).isEqualTo(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);
    }

    @Test
    public void 회원가입성공 () throws Exception {
        // given

        // when

        // then
    }
}