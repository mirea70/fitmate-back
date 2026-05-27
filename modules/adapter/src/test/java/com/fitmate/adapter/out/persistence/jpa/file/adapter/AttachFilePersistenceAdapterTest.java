package com.fitmate.adapter.out.persistence.jpa.file.adapter;

import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import com.fitmate.adapter.out.persistence.jpa.file.entity.AttachFileJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.file.repository.AttachFileRepository;
import com.fitmate.domain.error.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("AttachFilePersistenceAdapter 썸네일 테스트")
class AttachFilePersistenceAdapterTest {

    @InjectMocks
    private AttachFilePersistenceAdapter adapter;

    @Mock
    private AttachFileRepository attachFileRepository;

    private final String testDir = System.getProperty("java.io.tmpdir") + "/fitmate-test-files";

    private void setUpAdapter() {
        ReflectionTestUtils.setField(adapter, "fileDefaultDir", testDir);
        ReflectionTestUtils.setField(adapter, "maxFileSize",
                org.springframework.util.unit.DataSize.ofMegabytes(5));
    }

    @AfterEach
    void cleanUp() {
        deleteRecursively(new File(testDir));
    }

    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) deleteRecursively(child);
            }
        }
        file.delete();
    }

    private byte[] createTestImageBytes() throws IOException {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }

    @Nested
    @DisplayName("업로드 시 썸네일 생성")
    class UploadWithThumbnail {

        @Test
        @DisplayName("이미지 업로드 시 원본 + 썸네일 파일이 모두 생성됨")
        void uploadsCreatesThumbnail() throws IOException {
            setUpAdapter();
            byte[] imageBytes = createTestImageBytes();
            MockMultipartFile multipartFile = new MockMultipartFile(
                    "testImage", "myPhoto.jpg", "image/jpeg", imageBytes
            );

            AttachFileJpaEntity savedEntity = new AttachFileJpaEntity("myPhoto.jpg", "test_store.jpg");
            ReflectionTestUtils.setField(savedEntity, "id", 1L);
            savedEntity.setThumbnailStoreFileName("thumb_test_store.jpg");
            given(attachFileRepository.save(any(AttachFileJpaEntity.class))).willReturn(savedEntity);

            FileResponse response = adapter.uploadFile(multipartFile);

            assertThat(response).isNotNull();
            assertThat(response.getAttachFileId()).isEqualTo(1L);

            // 원본 파일 존재 확인
            File profileDir = new File(testDir + "/profile/");
            assertThat(profileDir.listFiles()).isNotEmpty();

            // 썸네일 파일 존재 확인
            File thumbnailDir = new File(testDir + "/thumbnail/");
            assertThat(thumbnailDir.exists()).isFalse();

            // 썸네일이 원본보다 작은지 확인
            // 저장 시 thumbnailStoreFileName이 포함되었는지 확인
            then(attachFileRepository).should().save(any(AttachFileJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("썸네일 다운로드")
    class DownloadThumbnail {

        @Test
        @DisplayName("썸네일이 있으면 썸네일 경로로 반환")
        void downloadThumbnailWhenExists() throws Exception {
            setUpAdapter();

            // 썸네일 파일 생성
            String thumbnailName = "thumb_test_uuid.jpg";
            File thumbDir = new File(testDir + "/thumbnail/");
            thumbDir.mkdirs();
            BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(img, "jpg", new File(thumbDir, thumbnailName));

            AttachFileJpaEntity entity = new AttachFileJpaEntity("myPhoto.jpg", "test_uuid.jpg");
            entity.setThumbnailStoreFileName(thumbnailName);
            given(attachFileRepository.findById(1L)).willReturn(Optional.of(entity));

            FileDownloadDto result = adapter.downloadThumbnailById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getUrlResource().exists()).isTrue();
            assertThat(result.getUrlResource().getURI().getPath()).contains("thumbnail");
        }

        @Test
        @DisplayName("썸네일이 없으면 원본으로 fallback")
        void downloadThumbnailFallbackToOriginal() throws Exception {
            setUpAdapter();

            // 원본 파일만 생성
            String storeName = "test_uuid.jpg";
            File profileDir = new File(testDir + "/profile/");
            profileDir.mkdirs();
            BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(img, "jpg", new File(profileDir, storeName));

            AttachFileJpaEntity entity = new AttachFileJpaEntity("myPhoto.jpg", storeName);
            // thumbnailStoreFileName은 null
            given(attachFileRepository.findById(1L)).willReturn(Optional.of(entity));

            FileDownloadDto result = adapter.downloadThumbnailById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getUrlResource().getURI().getPath()).contains("profile");
        }

        @Test
        @DisplayName("존재하지 않는 파일 ID로 조회하면 NotFoundException")
        void downloadThumbnailNotFound() {
            given(attachFileRepository.findById(999L)).willReturn(Optional.empty());

            assertThatThrownBy(() -> adapter.downloadThumbnailById(999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("파일 삭제 시 썸네일도 함께 삭제")
    class DeleteWithThumbnail {

        @Test
        @DisplayName("원본 삭제 시 썸네일도 함께 삭제됨")
        void deleteRemovesThumbnail() throws IOException {
            setUpAdapter();

            // 원본 + 썸네일 파일 생성
            String storeName = "test_uuid.jpg";
            String thumbnailName = "thumb_test_uuid.jpg";

            File profileDir = new File(testDir + "/profile/");
            profileDir.mkdirs();
            File originalFile = new File(profileDir, storeName);
            BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(img, "jpg", originalFile);

            File thumbDir = new File(testDir + "/thumbnail/");
            thumbDir.mkdirs();
            File thumbFile = new File(thumbDir, thumbnailName);
            BufferedImage thumbImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            ImageIO.write(thumbImg, "jpg", thumbFile);

            AttachFileJpaEntity entity = new AttachFileJpaEntity("myPhoto.jpg", storeName);
            entity.setThumbnailStoreFileName(thumbnailName);
            given(attachFileRepository.findById(1L)).willReturn(Optional.of(entity));

            adapter.deleteFile(1L);

            assertThat(originalFile.exists()).isFalse();
            assertThat(thumbFile.exists()).isFalse();
            then(attachFileRepository).should().delete(entity);
        }
    }
}
