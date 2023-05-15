package com.fitmate.app.account.controller;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.AccountMockMvcHelper;
import com.fitmate.app.account.service.JoinService;
import com.fitmate.app.exceptions.GlobalExceptionHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class JoinSuccessTest {
    @InjectMocks
    private AccountController target;
    @Mock
    private JoinService joinService;
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
    public void 회원가입성공 () throws Exception {
        // given
        final String url = "/api/accounts/join";
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        AccountDto.JoinResponse joinResponse = accountAppTestHelper.getTestJoinResponse();
        doReturn(joinResponse).when(joinService).join(any(AccountDto.JoinRequest.class));
        // when
        final ResultActions resultActions = accountMockMvcHelper.submitPost(joinRequest, url);
        // then
        final AccountDto.JoinResponse resultResponse = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), AccountDto.JoinResponse.class);

        resultActions.andExpect(status().isCreated());
        assertThat(resultResponse.getPrivateInfo().getEmail()).isEqualTo(joinResponse.getPrivateInfo().getEmail());
        assertThat(resultResponse.getRole()).isEqualTo(joinResponse.getRole());
        assertThat(resultResponse.getGender()).isEqualTo(joinResponse.getGender());
    }
}
