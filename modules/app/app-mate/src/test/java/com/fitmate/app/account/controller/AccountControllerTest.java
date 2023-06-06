package com.fitmate.app.account.controller;

import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.AccountMockMvcHelper;
import com.fitmate.app.mate.account.controller.AccountController;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.domain.account.service.AccountService;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @InjectMocks
    private AccountController target;
    @Mock
    private AccountService accountService;
    private AccountAppTestHelper accountAppTestHelper;
    private AccountMockMvcHelper accountMockMvcHelper;
    private Gson gson;

    @BeforeEach
    public void init() {
        accountAppTestHelper = new AccountAppTestHelper();
        accountMockMvcHelper = new AccountMockMvcHelper(target, new GlobalExceptionHandler());
        gson = new Gson();
    }

    @Test
    public void 회원조회실패_서비스에서에러호출 () throws Exception {
        // given
        String url = "/api/accounts/1";
        doThrow(new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA)).when(accountService).validateFindById(anyLong());
        // when
        ResultActions resultActions = accountMockMvcHelper.submitGet(url);
        // then
        resultActions.andExpect(status().isNotFound());
    }
}
