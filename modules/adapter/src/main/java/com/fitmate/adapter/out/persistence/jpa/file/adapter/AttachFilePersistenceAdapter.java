package com.fitmate.adapter.out.persistence.jpa.file.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.FileExtension;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
import com.fitmate.adapter.out.persistence.jpa.file.entity.AttachFileJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.file.repository.AttachFileRepository;
import com.fitmate.domain.error.exceptions.FileRequestException;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.FileErrorResult;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import com.fitmate.port.out.file.LoadAttachFilePort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class AttachFilePersistenceAdapter implements LoadAttachFilePort {
    private final AttachFileRepository attachFileRepository;
    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files";
    private final String profileImageDir = "/profile/";


    public FileResponse uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile == null) return null;

        String storeFileName = getStoreFileName(multipartFile);
        File file = new File(getFullPath(storeFileName));
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        multipartFile.transferTo(file);

        return saveToRepository(multipartFile.getOriginalFilename(), storeFileName);
    }

    public List<FileResponse> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
        if(multipartFiles == null || multipartFiles.isEmpty()) return  null;

        List<FileResponse> responses = new ArrayList<>();
        for(MultipartFile file : multipartFiles) {
            responses.add(this.uploadFile(file));
        }
        return responses;
    }

    private String getStoreFileName(MultipartFile file) {
        String rawFileName = file.getName();
        validateFileName(rawFileName);

        String uploadFileName = file.getOriginalFilename();
        assert uploadFileName != null;
        String extension = extractExt(uploadFileName);
        validateFileExtension(extension);

        return rawFileName + "_" + UUID.randomUUID() + "." + extension;
    }

    private void validateFileName(String rawFileName) {
        boolean isMatched = Pattern.matches("^[a-zA-Zㄱ-힣0-9]*$", rawFileName);
        if(!isMatched) throw new FileRequestException(FileErrorResult.NOT_MATCHING_NAME_RULE);
    }

    private void validateFileExtension(String requestExtension) {
        List<String> extensions = FileExtension.getExtensions();
        if(!extensions.contains(requestExtension))
            throw new FileRequestException(FileErrorResult.NOT_SUPPORT_EXT);
    }

    private String extractExt(String uploadFileName) {
        int dotIndex = uploadFileName.lastIndexOf(".");
        return uploadFileName.substring(dotIndex + 1);
    }

    private String getFullPath(String uploadFileName) {
        return fileDefaultDir + profileImageDir + uploadFileName;
    }

    private FileResponse saveToRepository(String uploadFileName, String storeFileName) {
        AttachFileJpaEntity newFile = new AttachFileJpaEntity(uploadFileName, storeFileName);
        AttachFileJpaEntity savedFile = attachFileRepository.save(newFile);

        return new FileResponse(savedFile.getId(), savedFile.getUploadFileName());
    }

    public FileDownloadDto downloadById(Long id) throws MalformedURLException {
        AttachFileJpaEntity attachFile = attachFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        return this.downloadFile(attachFile.getStoreFileName());
    }

    public FileDownloadDto downloadFile(String storeFileName) throws MalformedURLException {
        String uploadFileName = getUploadNameByStoreName(storeFileName);
        UrlResource urlResource = new UrlResource("file:" + getFullPath(storeFileName));
        String contentDisposition = getContentDisposition(uploadFileName);

        return new FileDownloadDto(urlResource, contentDisposition);
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

        AttachFileJpaEntity attachFile = attachFileRepository.findById(fileId)
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

    @Override
    public void checkExistFile(Long attachFileId) {
        if(attachFileId == null)
            throw new IllegalArgumentException("fileId not to be null");
        AttachFileJpaEntity fileJpaEntity = attachFileRepository.getById(attachFileId);
        String path = getFullPath(fileJpaEntity.getStoreFileName());
        File file = new File(path);
        if(!file.exists())
            throw new NotFoundException(NotFoundErrorResult.NOT_EXIST_FILE_PATH);
    }

    @Override
    public void checkExistFiles(Set<Long> fileIds) {
        for(Long fileId : fileIds)
            this.checkExistFile(fileId);
    }
}
