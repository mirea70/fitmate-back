package com.fitmate.app.file.controller;


import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.file.helper.FileMockMvcHelper;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.file.controller.FileController;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {
    @InjectMocks
    private FileController target;
    @Mock
    private FileService fileService;

    private FileTestHelper fileTestHelper;

    private FileMockMvcHelper fileMockMvcHelper;


    @BeforeEach
    public void init() {
        fileTestHelper = new FileTestHelper();
        fileMockMvcHelper = new FileMockMvcHelper(target, new GlobalExceptionHandler());
    }

    @Test
    public void 파일다운로드_ID로_성공 () throws Exception {
        // given
        AttachFileDto.Download downloadDto = fileTestHelper.getTestDownloadDto();
        final String url = "/api/files/1";
        doReturn(downloadDto).when(fileService).downloadById(anyLong());
        // when
        ResultActions resultActions = fileMockMvcHelper.submitGet(url);
        // then
        resultActions.andExpect(status().isOk());
        String source = resultActions.andReturn().getResponse().getHeader("Content-Disposition");
        assertThat(source).isEqualTo(downloadDto.getContentDisposition());
    }

    @Test
    public void 파일다운로드_ID로_실패 () throws Exception {
        // given
        AttachFileDto.Download downloadDto = fileTestHelper.getTestDownloadDto();
        final String url = "/api/files/1";
        doThrow(MalformedURLException.class).when(fileService).downloadById(anyLong());
        // when
        ResultActions resultActions = fileMockMvcHelper.submitGet(url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
