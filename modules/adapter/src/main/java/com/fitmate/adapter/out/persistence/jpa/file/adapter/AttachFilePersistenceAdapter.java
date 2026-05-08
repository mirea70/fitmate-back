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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import net.coobird.thumbnailator.Thumbnails;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class AttachFilePersistenceAdapter implements LoadAttachFilePort {
    private final AttachFileRepository attachFileRepository;
    private final String rootPath = System.getProperty("user.home");
    private final String fileDefaultDir = rootPath + "/files";
    private final String profileImageDir = "/profile/";
    private final String thumbnailDir = "/thumbnail/";
    private static final int THUMBNAIL_WIDTH = 200;
    private static final int THUMBNAIL_HEIGHT = 200;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    public FileResponse uploadFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile == null) return null;
        validateFileSize(multipartFile);

        String storeFileName = getStoreFileName(multipartFile);
        File file = new File(getFullPath(storeFileName));
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        multipartFile.transferTo(file);

        String thumbnailStoreFileName = generateThumbnail(file, storeFileName);
        return saveToRepository(multipartFile.getOriginalFilename(), storeFileName, thumbnailStoreFileName);
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

    private void validateFileSize(MultipartFile multipartFile) {
        if(multipartFile.getSize() > maxFileSize.toBytes())
            throw new FileRequestException(FileErrorResult.TOO_LARGE_SIZE);
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

    private String getFullPath(String fileName) {
        return fileDefaultDir + profileImageDir + fileName;
    }

    private String getThumbnailFullPath(String fileName) {
        return fileDefaultDir + thumbnailDir + fileName;
    }

    private String generateThumbnail(File originalFile, String storeFileName) {
        try {
            String thumbnailStoreFileName = "thumb_" + storeFileName;
            File thumbnailFile = new File(getThumbnailFullPath(thumbnailStoreFileName));
            if (!thumbnailFile.getParentFile().exists())
                thumbnailFile.getParentFile().mkdirs();

            Thumbnails.of(originalFile)
                    .size(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                    .toFile(thumbnailFile);

            return thumbnailStoreFileName;
        } catch (IOException e) {
            log.warn("썸네일 생성 실패: {}", storeFileName, e);
            return null;
        }
    }

    private FileResponse saveToRepository(String uploadFileName, String storeFileName, String thumbnailStoreFileName) {
        AttachFileJpaEntity newFile = new AttachFileJpaEntity(uploadFileName, storeFileName);
        if (thumbnailStoreFileName != null) {
            newFile.setThumbnailStoreFileName(thumbnailStoreFileName);
        }
        AttachFileJpaEntity savedFile = attachFileRepository.save(newFile);

        return new FileResponse(savedFile.getId(), savedFile.getUploadFileName());
    }

    public FileDownloadDto downloadById(Long id) throws MalformedURLException {
        AttachFileJpaEntity attachFile = attachFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        return this.downloadFile(attachFile.getStoreFileName());
    }

    public FileDownloadDto downloadThumbnailById(Long id) throws MalformedURLException {
        AttachFileJpaEntity attachFile = attachFileRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));

        String thumbnailName = attachFile.getThumbnailStoreFileName();
        if (thumbnailName == null) {
            return this.downloadFile(attachFile.getStoreFileName());
        }

        String uploadFileName = getUploadNameByStoreName(attachFile.getStoreFileName());
        UrlResource urlResource = new UrlResource(new File(getThumbnailFullPath(thumbnailName)).toURI());
        String contentDisposition = getContentDisposition(uploadFileName);

        return new FileDownloadDto(urlResource, contentDisposition);
    }

    public FileDownloadDto downloadFile(String storeFileName) throws MalformedURLException {
        String uploadFileName = getUploadNameByStoreName(storeFileName);
        UrlResource urlResource = new UrlResource(new File(getFullPath(storeFileName)).toURI());
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
                deleteThumbnailFile(attachFile.getThumbnailStoreFileName());
                attachFileRepository.delete(attachFile);
            }
            else {
                throw new FileRequestException(FileErrorResult.FAIL_REMOVE_FILE);
            }
        } else {
            throw new NotFoundException(NotFoundErrorResult.NOT_EXIST_FILE_PATH);
        }
    }

    private void deleteThumbnailFile(String thumbnailStoreFileName) {
        if (thumbnailStoreFileName == null) return;
        File thumbnailFile = new File(getThumbnailFullPath(thumbnailStoreFileName));
        if (thumbnailFile.exists()) {
            thumbnailFile.delete();
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
