package com.fitmate.app.mate.file.service;

import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.mapper.AttachFileDtoMapper;
import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final AttachFileRepository attachFileRepository;
    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files/";
    private final String profileImageDir = "/profile/";
    public AttachFileDto.Response uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile == null) return null;

        String uploadFileName = multipartFile.getName();
        String storeFileName = UUID.randomUUID() + "." + extractExt(uploadFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        AttachFile newFile = AttachFile.builder()
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .build();

        AttachFile savedFile = attachFileRepository.save(newFile);

        return AttachFileDtoMapper.INSTANCE.toResponse(savedFile);
    }

    private String extractExt(String uploadFileName) {
        int dotIndex = uploadFileName.lastIndexOf(".");
        return uploadFileName.substring(dotIndex + 1);
    }

    private String getFullPath(String uploadFileName) {
        return fileDefaultDir + profileImageDir + uploadFileName;
    }
}
