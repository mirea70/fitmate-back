package com.fitmate.app.account.helper;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.domain.file.entity.AttachFile;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    public AttachFileDto.Download getTestDownloadDto() throws MalformedURLException {
        String uploadFileName = "image.png";
        String storeFileName = "image-3b36e346-2e82-4c6c-9ae7-6076d54c43a9.png";
        return this.getTestDownloadDto(uploadFileName, storeFileName);
    }

    public AttachFileDto.Download getTestDownloadDto(String uploadFileName, String storeFileName) throws MalformedURLException {
        return AttachFileDto.Download.builder()
                .contentDisposition(getContentDisposition(uploadFileName))
                .urlResource(new UrlResource("file:" + getFullPath(storeFileName)))
                .build();
    }

    private String getContentDisposition(String uploadFileName) {
        String encodedUploadFileName = URLEncoder.encode(uploadFileName, StandardCharsets.UTF_8);
        return "attachment; filename=\"" + encodedUploadFileName + "\"";
    }

    public String getFullPath(String storeFileName) {
        return fileDefaultDir + profileImageDir + storeFileName;
    }

    public AttachFile getTestAttachFile() {
        return AttachFile.builder()
                .id(1L)
                .uploadFileName("image.png")
                .storeFileName("image-3b36e346-2e82-4c6c-9ae7-6076d54c43a9.png")
                .build();
    }
}
