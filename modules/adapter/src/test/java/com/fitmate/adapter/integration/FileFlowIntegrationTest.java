package com.fitmate.adapter.integration;

import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import com.fitmate.adapter.out.persistence.jpa.file.entity.AttachFileJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.file.repository.AttachFileRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[통합] 파일 업로드/썸네일 플로우")
class FileFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AttachFilePersistenceAdapter filePersistenceAdapter;

    @Autowired
    private AttachFileRepository attachFileRepository;

    private final String fileDir = System.getProperty("user.home") + "/files";

    @AfterEach
    void cleanUpFiles() {
        // 테스트에서 생성된 파일 정리
        deleteDir(new File(fileDir + "/profile/"));
        deleteDir(new File(fileDir + "/thumbnail/"));
    }

    private void deleteDir(File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) f.delete();
            }
        }
    }

    private MockMultipartFile createTestImage(String name) throws IOException {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return new MockMultipartFile(name, name + ".jpg", "image/jpeg", baos.toByteArray());
    }

    @Nested
    @DisplayName("업로드 → 썸네일 생성 플로우")
    class UploadFlow {

        @Test
        @DisplayName("이미지 업로드 시 DB에 썸네일 파일명이 저장됨")
        @Transactional
        void uploadSavesThumbnailToDb() throws IOException {
            MockMultipartFile file = createTestImage("testImage");

            FileResponse response = filePersistenceAdapter.uploadFile(file);

            assertThat(response).isNotNull();
            assertThat(response.getAttachFileId()).isNotNull();

            Optional<AttachFileJpaEntity> entity = attachFileRepository.findById(response.getAttachFileId());
            assertThat(entity).isPresent();
            assertThat(entity.get().getThumbnailStoreFileName()).isNotNull();
            assertThat(entity.get().getThumbnailStoreFileName()).startsWith("thumb_");
        }

        @Test
        @DisplayName("업로드 후 썸네일 파일이 디스크에 실제 존재")
        void uploadCreatesThumbnailFile() throws IOException {
            MockMultipartFile file = createTestImage("testImage");

            filePersistenceAdapter.uploadFile(file);

            File thumbnailDir = new File(fileDir + "/thumbnail/");
            assertThat(thumbnailDir.exists()).isTrue();
            File[] thumbnails = thumbnailDir.listFiles();
            assertThat(thumbnails).isNotEmpty();
            assertThat(thumbnails[0].getName()).startsWith("thumb_");
        }
    }

    @Nested
    @DisplayName("썸네일 다운로드 플로우")
    class DownloadFlow {

        @Test
        @DisplayName("업로드 → 썸네일 다운로드 전체 플로우")
        void uploadAndDownloadThumbnail() throws Exception {
            MockMultipartFile file = createTestImage("testImage");
            FileResponse response = filePersistenceAdapter.uploadFile(file);

            FileDownloadDto thumbnailDto = filePersistenceAdapter.downloadThumbnailById(response.getAttachFileId());

            assertThat(thumbnailDto).isNotNull();
            assertThat(thumbnailDto.getUrlResource().exists()).isTrue();
            assertThat(thumbnailDto.getUrlResource().getURI().getPath()).contains("thumbnail");
        }

        @Test
        @DisplayName("업로드 → 원본 다운로드는 기존대로 동작")
        void uploadAndDownloadOriginal() throws Exception {
            MockMultipartFile file = createTestImage("testImage");
            FileResponse response = filePersistenceAdapter.uploadFile(file);

            FileDownloadDto originalDto = filePersistenceAdapter.downloadById(response.getAttachFileId());

            assertThat(originalDto).isNotNull();
            assertThat(originalDto.getUrlResource().exists()).isTrue();
            assertThat(originalDto.getUrlResource().getURI().getPath()).contains("profile");
        }
    }

    @Nested
    @DisplayName("삭제 플로우")
    class DeleteFlow {

        @Test
        @DisplayName("파일 삭제 시 원본 + 썸네일 모두 제거됨")
        void deleteRemovesBothFiles() throws IOException {
            MockMultipartFile file = createTestImage("testImage");
            FileResponse response = filePersistenceAdapter.uploadFile(file);

            // 삭제 전 파일 존재 확인
            File profileDir = new File(fileDir + "/profile/");
            File thumbnailDir = new File(fileDir + "/thumbnail/");
            assertThat(profileDir.listFiles()).isNotEmpty();
            assertThat(thumbnailDir.listFiles()).isNotEmpty();

            filePersistenceAdapter.deleteFile(response.getAttachFileId());

            // 삭제 후 파일 제거 확인
            File[] remainingOriginals = profileDir.listFiles();
            File[] remainingThumbnails = thumbnailDir.listFiles();
            assertThat(remainingOriginals == null || remainingOriginals.length == 0).isTrue();
            assertThat(remainingThumbnails == null || remainingThumbnails.length == 0).isTrue();
        }
    }
}
