package com.fitmate.app.account.service;

import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.account.service.JoinService;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.result.DuplicatedErrorResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @Mock
    private FileService fileService;
    private final FileTestHelper fileTestHelper = new FileTestHelper();
    private final AccountAppTestHelper accountAppTestHelper = new AccountAppTestHelper();
    @Mock
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void 회원가입실패_값중복 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        MockMultipartFile mockMultipartFile = (MockMultipartFile) joinRequest.getProfileImage();
        AttachFileDto.Response fileResponse = fileTestHelper.getTestResponseDto(mockMultipartFile.getOriginalFilename());

        doThrow(DataIntegrityViolationException.class).when(accountRepository).save(any());
        doReturn(fileResponse).when(fileService).uploadFile(mockMultipartFile);
        // when
        final DuplicatedException result = assertThrows(DuplicatedException.class,
                () -> target.join(joinRequest));
        // then
        assertEquals(DuplicatedException.class, result.getClass());
        assertThat(result.getErrorResult()).isEqualTo(DuplicatedErrorResult.DUPLICATED_ACCOUNT_VALUE);
    }

    @Test
    public void 회원가입성공 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        Account account = AccountDtoMapper.INSTANCE.toEntity(joinRequest);
        MockMultipartFile mockMultipartFile = (MockMultipartFile) joinRequest.getProfileImage();
        AttachFileDto.Response fileResponse = fileTestHelper.getTestResponseDto(mockMultipartFile.getOriginalFilename());

        doReturn(account).when(accountRepository).save(any(Account.class));
        doReturn(fileResponse).when(fileService).uploadFile(mockMultipartFile);
        // when
        final AccountDto.JoinResponse result = target.join(joinRequest);
        // then
        assertThat(result).isNotNull();
        assertEquals(result.getPrivateInfo().getEmail(), joinRequest.getPrivateInfo().getEmail());
        assertThat(result.getProfileInfo().getProfileImageId()).isEqualTo(1L);

        verify(accountService, times(1)).CheckDuplicated(any(AccountDuplicateCheckDto.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}