package com.fitmate.domain.file.helper;

import com.fitmate.domain.file.entity.AttachFile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AttachFileDomainTestHelper {
    public AttachFile getTestFile() {
        String uploadFileName = "업로드파일1";
        return AttachFile.builder()
                .id(1L)
                .uploadFileName(uploadFileName)
                .storeFileName(UUID.randomUUID().toString())
                .path("path")
                .build();
    }
}
