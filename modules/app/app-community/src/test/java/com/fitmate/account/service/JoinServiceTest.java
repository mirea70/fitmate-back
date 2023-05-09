package com.fitmate.account.service;

import com.fitmate.account.dto.AccountDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
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

    @Test
    public void 회원가입실패_이미존재함 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = getTestAccountJoinRequest();

        // when
        final AccountDuplicatedException result = assertThrows(AccountDuplicatedException.class,
                () -> target.join(joinRequest));
        // then
        assertThat(result.getErrorResult()).isEqualTo(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN);
    }

    @Test
    public void 회원가입실패_값중복 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = getTestAccountJoinRequest();

        doThrow(DataIntegrityViolationException.class).when(accountRepository).save(any());
        // when
        final AccountDuplicatedException result = assertThrows(AccountDuplicatedException.class,
                () -> target.join(joinRequest));
        // then
        assertEquals(AccountDuplicatedException.class, result.getClass());
        assertThat(result.getErrorResult()).isEqualTo(AccountErrorResult.DUPLICATED_ACCOUNT_VALUE);
    }

    private AccountDto.JoinRequest getTestAccountJoinRequest() {
        PrivateInfo privateInfo = PrivateInfo.builder()
                .name("미이수")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("닉네임2")
                .build();

        return AccountDto.JoinRequest.builder()
                        .loginName("abc2")
                        .password("12345678")
                        .privateInfo(privateInfo)
                        .profileInfo(profileInfo)
                        .role(AccountRole.USER)
                        .gender(Gender.MAIL)
                        .build();
    }

    private Account getAccountByRequestDto(AccountDto.JoinRequest joinRequest) {

        return Account.builder()
                .loginName(joinRequest.getLoginName())
                .password(Password.builder().value(joinRequest.getPassword()).build())
                .privateInfo(joinRequest.getPrivateInfo())
                .profileInfo(joinRequest.getProfileInfo())
                .gender(joinRequest.getGender())
                .role(joinRequest.getRole())
                .build();
    }

    @Test
    public void 회원가입성공 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = getTestAccountJoinRequest();
        Account account = getAccountByRequestDto(joinRequest);
        doReturn(account).when(accountRepository).save(any(Account.class));
        // when
        final AccountDto.JoinResponse result = target.join(joinRequest);
        // then
        assertThat(result).isNotNull();
        assertEquals(result.getPrivateInfo().getEmail(), joinRequest.getPrivateInfo().getEmail());

        verify(accountService, times(1)).CheckDuplicatedByEmail(joinRequest.getPrivateInfo().getEmail());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}