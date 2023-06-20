package com.fitmate.app.mate.file.service;

import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.mapper.AttachFileDtoMapper;
import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.enums.FileExtension;
import com.fitmate.domain.file.repository.AttachFileRepository;
import com.fitmate.exceptions.exception.FileRequestException;
import com.fitmate.exceptions.result.FileErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

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

        String storeFileName = getStoreFileName(multipartFile);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return saveToRepository(multipartFile.getOriginalFilename(), storeFileName);
    }

    private String getStoreFileName(MultipartFile multipartFile) {
        String rawFileName = multipartFile.getName();
        validateFileName(rawFileName);

        String uploadFileName = multipartFile.getOriginalFilename();
        assert uploadFileName != null;
        String extension = extractExt(uploadFileName);
        validateFileExtension(extension);

        return rawFileName + "_" + UUID.randomUUID() + "." + extension;
    }

    private void validateFileExtension(String requestExtension) {
        List<String> extensions = FileExtension.getExtensions();
        if(!extensions.contains(requestExtension))
            throw new FileRequestException(FileErrorResult.NOT_SUPPORT_EXT);
    }

    private void validateFileName(String rawFileName) {
        boolean isMatched = Pattern.matches("^[a-zA-Zㄱ-힣0-9]*$", rawFileName);
        if(!isMatched) throw new FileRequestException(FileErrorResult.NOT_MATCHING_NAME_RULE);
    }

    private String extractExt(String uploadFileName) {
        int dotIndex = uploadFileName.lastIndexOf(".");
        return uploadFileName.substring(dotIndex + 1);
    }

    private String getFullPath(String uploadFileName) {
        return fileDefaultDir + profileImageDir + uploadFileName;
    }

    private AttachFileDto.Response saveToRepository(String uploadFileName, String storeFileName) {
        AttachFile newFile = AttachFileDtoMapper.INSTANCE.toEntityByFileInfo(uploadFileName, storeFileName);
        AttachFile savedFile = attachFileRepository.save(newFile);

        return AttachFileDtoMapper.INSTANCE.toResponse(savedFile);
    }

    public AttachFileDto.Download downloadFile(String storeFileName) throws MalformedURLException {
        String uploadFileName = getUploadNameByStoreName(storeFileName);
        UrlResource urlResource = new UrlResource("file:" + getFullPath(storeFileName));
        String contentDisposition = getContentDisposition(uploadFileName);

        return AttachFileDtoMapper.INSTANCE.toDownLoadDto(urlResource, contentDisposition);
    }

    private String getUploadNameByStoreName(String storeFileName) {
        return storeFileName.split("_")[0];
    }

    private String getContentDisposition(String uploadFileName) {
        String encodedUploadFileName = URLEncoder.encode(uploadFileName, StandardCharsets.UTF_8);
        return "attachment; filename=\"" + encodedUploadFileName + "\"";
    }
}
