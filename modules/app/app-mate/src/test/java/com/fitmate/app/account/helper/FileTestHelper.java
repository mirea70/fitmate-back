package com.fitmate.app.account.helper;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Component
public class FileTestHelper {

    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files/";
    private final String profileImageDir = "/profile/";


    public AttachFileDto.Response getTestResponseDto(String uploadFileName) {
        int dotIndex = uploadFileName.lastIndexOf(".");
        String ext = uploadFileName.substring(dotIndex + 1);
        String rawFileName = uploadFileName.substring(0, dotIndex);
        String storeFileName = rawFileName + UUID.randomUUID() + "." + ext;

        return AttachFileDto.Response.builder()
                .id(1L)
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .build();
    }

    public MockMultipartFile getMockMultipartFile(String fileName) throws Exception {
        String ext = "png";
        String path = fileDefaultDir + profileImageDir + fileName + "." + ext;

        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + ext, ext, fileInputStream);
    }

    public MockMultipartFile getMockMultipartFile(String fileName, String ext, String path) throws Exception {

        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + ext, ext, fileInputStream);
    }
}
