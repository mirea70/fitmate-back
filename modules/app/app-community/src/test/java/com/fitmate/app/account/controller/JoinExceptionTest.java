package com.fitmate.app.account.controller;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.AccountMockMvcHelper;
import com.fitmate.app.account.service.JoinService;
import com.fitmate.app.exceptions.GlobalExceptionHandler;
import com.fitmate.exceptions.exception.AccountDuplicatedException;
import com.fitmate.exceptions.result.AccountErrorResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class JoinExceptionTest {
    @InjectMocks
    private AccountController target;
    @Mock
    private JoinService joinService;
    private AccountAppTestHelper accountAppTestHelper;
    private AccountMockMvcHelper accountMockMvcHelper;

    @BeforeEach
    public void init() {
        accountAppTestHelper = new AccountAppTestHelper();
        accountMockMvcHelper = new AccountMockMvcHelper(target, new GlobalExceptionHandler());
    }

    @Test
    public void 회원가입실패_JoinService_AccountDuplicatedException_Join () throws Exception {
        // given
        final String url = "/api/accounts/join";
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        doThrow(new AccountDuplicatedException(AccountErrorResult.DUPLICATED_ACCOUNT_JOIN))
                .when(joinService).join(any(AccountDto.JoinRequest.class));
        // when
        final ResultActions resultActions = accountMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
