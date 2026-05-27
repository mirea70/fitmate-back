package com.fitmate.adapter.integration;

import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import com.fitmate.adapter.out.persistence.jpa.file.entity.AttachFileJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.file.repository.AttachFileRepository;
import com.fitmate.adapter.out.persistence.jpa.job.JobStatus;
import com.fitmate.adapter.out.persistence.jpa.job.JobType;
import com.fitmate.adapter.out.persistence.jpa.job.entity.JobQueueJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.job.repository.JobQueueRepository;
import com.fitmate.adapter.out.persistence.jpa.job.scheduler.ImageResizingJobScheduler;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Integration] file upload and thumbnail flow")
class FileFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AttachFilePersistenceAdapter filePersistenceAdapter;

    @Autowired
    private AttachFileRepository attachFileRepository;

    @Autowired
    private JobQueueRepository jobQueueRepository;

    @Autowired
    private ImageResizingJobScheduler imageResizingJobScheduler;

    private final String fileDir = System.getProperty("user.home") + "/files";

    @AfterEach
    void cleanUpFiles() {
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

    private void enqueueImageResizingJob(Long attachFileId) {
        jobQueueRepository.save(new JobQueueJpaEntity(JobType.IMAGE_RESIZING, attachFileId));
    }

    @Nested
    @DisplayName("upload")
    class UploadFlow {

        @Test
        @DisplayName("upload saves original without creating thumbnail job directly")
        @Transactional
        void uploadSavesOriginalOnly() throws IOException {
            MockMultipartFile file = createTestImage("testImage");

            FileResponse response = filePersistenceAdapter.uploadFile(file);

            assertThat(response).isNotNull();
            assertThat(response.getAttachFileId()).isNotNull();

            Optional<AttachFileJpaEntity> entity = attachFileRepository.findById(response.getAttachFileId());
            assertThat(entity).isPresent();
            assertThat(entity.get().getThumbnailStoreFileName()).isNull();
            assertThat(jobQueueRepository
                    .findTop20ByJobTypeAndStatusInAndRetryCountLessThanOrderByCreatedAtAsc(
                            JobType.IMAGE_RESIZING,
                            List.of(JobStatus.PENDING),
                            3
                    )).isEmpty();
        }

        @Test
        @DisplayName("scheduler creates thumbnail for queued image resizing job")
        void schedulerCreatesThumbnailFile() throws IOException {
            MockMultipartFile file = createTestImage("testImage");
            FileResponse response = filePersistenceAdapter.uploadFile(file);
            enqueueImageResizingJob(response.getAttachFileId());

            imageResizingJobScheduler.processImageResizingJobs();

            File thumbnailDir = new File(fileDir + "/thumbnail/");
            assertThat(thumbnailDir.exists()).isTrue();
            File[] thumbnails = thumbnailDir.listFiles();
            assertThat(thumbnails).isNotEmpty();
            assertThat(thumbnails[0].getName()).startsWith("thumb_");
        }
    }

    @Nested
    @DisplayName("download")
    class DownloadFlow {

        @Test
        @DisplayName("thumbnail download returns generated thumbnail")
        void uploadAndDownloadThumbnail() throws Exception {
            MockMultipartFile file = createTestImage("testImage");
            FileResponse response = filePersistenceAdapter.uploadFile(file);
            enqueueImageResizingJob(response.getAttachFileId());
            imageResizingJobScheduler.processImageResizingJobs();

            FileDownloadDto thumbnailDto = filePersistenceAdapter.downloadThumbnailById(response.getAttachFileId());

            assertThat(thumbnailDto).isNotNull();
            assertThat(thumbnailDto.getUrlResource().exists()).isTrue();
            assertThat(thumbnailDto.getUrlResource().getURI().getPath()).contains("thumbnail");
        }

        @Test
        @DisplayName("original download keeps existing behavior")
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
    @DisplayName("delete")
    class DeleteFlow {

        @Test
        @DisplayName("delete removes original and generated thumbnail")
        void deleteRemovesBothFiles() throws IOException {
            MockMultipartFile file = createTestImage("testImage");
            FileResponse response = filePersistenceAdapter.uploadFile(file);
            enqueueImageResizingJob(response.getAttachFileId());
            imageResizingJobScheduler.processImageResizingJobs();

            File profileDir = new File(fileDir + "/profile/");
            File thumbnailDir = new File(fileDir + "/thumbnail/");
            assertThat(profileDir.listFiles()).isNotEmpty();
            assertThat(thumbnailDir.listFiles()).isNotEmpty();

            filePersistenceAdapter.deleteFile(response.getAttachFileId());

            File[] remainingOriginals = profileDir.listFiles();
            File[] remainingThumbnails = thumbnailDir.listFiles();
            assertThat(remainingOriginals == null || remainingOriginals.length == 0).isTrue();
            assertThat(remainingThumbnails == null || remainingThumbnails.length == 0).isTrue();
        }
    }
}
