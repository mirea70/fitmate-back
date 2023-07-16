package com.fitmate.app.account.controller;

import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.AccountMockMvcHelper;
import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.account.controller.AccountController;
import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.service.AccountProfileService;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.config.GsonUtil;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.test.web.servlet.ResultActions;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @InjectMocks
    private AccountController target;
    @Mock
    private AccountService accountService;
    @Mock
    private AccountProfileService accountProfileService;
    private AccountAppTestHelper accountAppTestHelper;
    private AccountMockMvcHelper accountMockMvcHelper;
    private FileTestHelper fileTestHelper;
    private Gson gson;

    @BeforeEach
    public void init() {
        accountAppTestHelper = new AccountAppTestHelper();
        accountMockMvcHelper = new AccountMockMvcHelper(target, new GlobalExceptionHandler());
        fileTestHelper = new FileTestHelper();
        gson = GsonUtil.buildGson();
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

    @Test
    public void 프로필이미지_다운로드_성공 () throws Exception {
        // given
        AttachFileDto.Download downloadDto = fileTestHelper.getTestDownloadDto();
        final String url = "/api/accounts/1/image";
        doReturn(downloadDto).when(accountProfileService).downloadProfileImage(anyLong());
        // when
        ResultActions resultActions = accountMockMvcHelper.submitGet(url);
        // then
        resultActions.andExpect(status().isOk());
        String source = resultActions.andReturn().getResponse().getHeader("Content-Disposition");
        assertThat(source).isEqualTo(downloadDto.getContentDisposition());
    }

    @Test
    public void 프로필이미지_다운로드_실패 () throws Exception {
        // given
        AttachFileDto.Download downloadDto = fileTestHelper.getTestDownloadDto();
        final String url = "/api/accounts/1/image";
        doThrow(MalformedURLException.class).when(accountProfileService).downloadProfileImage(anyLong());
        // when
        ResultActions resultActions = accountMockMvcHelper.submitGet(url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
