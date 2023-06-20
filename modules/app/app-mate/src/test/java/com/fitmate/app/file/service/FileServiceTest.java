package com.fitmate.app.file.service;

import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.exceptions.exception.FileRequestException;
import com.fitmate.exceptions.result.FileErrorResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService target;

    @Autowired
    private FileTestHelper fileTestHelper;

    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files";
    private final String profileImageDir = "/profile/";

    @ParameterizedTest
    @ValueSource(strings = {
            "-", "_","+","=","!","@","#","#","$","%","{","}","[","]"
            ,"^","&","*","(",")",";","'","\"","\\","|",",","<",".",">","?","`","~"
    })
    public void 파일업로드실패_파일이름에특수문자포함 (final String specialWord) throws Exception {
        // given
        String uploadFileName = "image" + specialWord;
        String ext = "png";
        String path = fileDefaultDir + profileImageDir + uploadFileName + "." + ext;
        MockMultipartFile multipartFile = fileTestHelper.getMockMultipartFile(uploadFileName);
        // when
        final FileRequestException result = assertThrows(FileRequestException.class, () -> target.uploadFile(multipartFile));
        // then
        assertThat(result.getErrorResult()).isEqualTo(FileErrorResult.NOT_MATCHING_NAME_RULE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"zip","doc","docs"})
    public void 파일업로드실패_지원X확장자 (String requestExt) throws Exception {
        // given
        String uploadFileName = "image";
        String ext = requestExt;
        String path = fileDefaultDir + profileImageDir + uploadFileName + "." + ext;
        MockMultipartFile multipartFile = fileTestHelper.getMockMultipartFile(uploadFileName,ext,path);
        // when
        final FileRequestException result = assertThrows(FileRequestException.class, () -> target.uploadFile(multipartFile));
        // then
        assertThat(result.getErrorResult()).isEqualTo(FileErrorResult.NOT_SUPPORT_EXT);
    }

    @Test
    public void 파일업로드_성공 () throws Exception {
        // given
        String uploadFileName = "image";
        String ext = "png";
        String path = fileDefaultDir + profileImageDir + uploadFileName + "." + ext;
        MockMultipartFile multipartFile = fileTestHelper.getMockMultipartFile(uploadFileName);
        // when
        AttachFileDto.Response result = target.uploadFile(multipartFile);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getUploadFileName()).isEqualTo(multipartFile.getOriginalFilename());
    }

    @Test
    public void 파일다운로드 () throws Exception {
        // given
        String storeFileName = "image_2368d0ba-1f03-4b55-b51a-aa643cea88b9.png";
        String uploadFileName = storeFileName.split("_")[0];
        String path = fileDefaultDir + profileImageDir + storeFileName;
        // when
        AttachFileDto.Download result = target.downloadFile(storeFileName);
        // then
        assertThat(result.getUrlResource().getURL().toString()).isEqualTo("file:" + path);
        System.out.println("result = " + result.getUrlResource());
        assertThat(result.getContentDisposition()).isEqualTo("attachment; filename=\"" + uploadFileName + "\"");
    }
}
