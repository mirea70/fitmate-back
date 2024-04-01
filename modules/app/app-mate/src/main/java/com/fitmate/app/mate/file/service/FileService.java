package com.fitmate.app.mate.file.service;

import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.mapper.AttachFileDtoMapper;
import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.enums.FileExtension;
import com.fitmate.domain.file.repository.AttachFileRepository;
import com.fitmate.exceptions.exception.FileRequestException;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.FileErrorResult;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final AttachFileRepository attachFileRepository;
    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files";
    private final String profileImageDir = "/profile/";


    public AttachFileDto.Response uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile == null) return null;

        String storeFileName = getStoreFileName(multipartFile);
        File file = new File(getFullPath(storeFileName));
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        multipartFile.transferTo(file);

        return saveToRepository(multipartFile.getOriginalFilename(), storeFileName);
    }

    public List<AttachFileDto.Response> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        if(multipartFiles == null || multipartFiles.isEmpty()) return  null;

        List<AttachFileDto.Response> responses = new ArrayList<>();
        for(MultipartFile file : multipartFiles) {
            responses.add(this.uploadFile(file));
        }
        return responses;
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

    public AttachFileDto.Download downloadById(Long id) throws MalformedURLException {
        AttachFile attachFile = attachFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        return this.downloadFile(attachFile.getStoreFileName());
    }

    public AttachFileDto.Download downloadFile(String storeFileName) throws MalformedURLException {
        String uploadFileName = getUploadNameByStoreName(storeFileName);
        UrlResource urlResource = new UrlResource("file:" + getFullPath(storeFileName));
        String contentDisposition = getContentDisposition(uploadFileName);

        return AttachFileDtoMapper.INSTANCE.toDownLoadDto(urlResource, contentDisposition);
    }

    private String getUploadNameByStoreName(String storeFileName) {
        String[] arr1 = storeFileName.split("_");
        String[] arr2 = arr1[1].split("\\.");
        String name = arr1[0];
        String ext = arr2[1];
        return name + "." + ext;
    }

    private String getContentDisposition(String uploadFileName) {
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        return "attachment; filename=\"" + encodedUploadFileName + "\"";
    }

    public void deleteFile(Long fileId) {
        if(fileId == null) return;

        AttachFile attachFile = attachFileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));

        File file = new File(getFullPath(attachFile.getStoreFileName()));
        if(file.exists()) {
            if(file.delete()) {
               attachFileRepository.delete(attachFile);
            }
            else {
                throw new FileRequestException(FileErrorResult.FAIL_REMOVE_FILE);
            }
        } else {
            throw new NotFoundException(NotFoundErrorResult.NOT_EXIST_FILE_PATH);
        }
    }
}
