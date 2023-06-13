package com.fitmate.app.file.service;

import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files/";
    private final String profileImageDir = "/profile/";

    @Test
    public void 파일업로드 () throws Exception {
        // given
        String uploadFileName = "image";
        String ext = "png";
        String path = fileDefaultDir + profileImageDir + uploadFileName + "." + ext;
        MockMultipartFile multipartFile = getMockMultipartFile(uploadFileName, ext, path);
        // when
        AttachFileDto.Response result = fileService.uploadFile(multipartFile);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getUploadFileName() + "." + ext).isEqualTo(multipartFile.getOriginalFilename());
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String ext, String path) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + ext, ext, fileInputStream);
    }
}
