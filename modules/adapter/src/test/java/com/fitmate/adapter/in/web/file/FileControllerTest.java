package com.fitmate.adapter.in.web.file;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import com.fitmate.domain.error.exceptions.NotFoundException;
import com.fitmate.domain.error.results.NotFoundErrorResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FileController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("FileController 테스트")
class FileControllerTest extends BaseControllerTest {

    @MockBean
    private AttachFilePersistenceAdapter attachFilePersistenceAdapter;

    @TempDir
    Path tempDir;

    private UrlResource createTempFileResource(String fileName) throws IOException {
        File file = tempDir.resolve(fileName).toFile();
        Files.write(file.toPath(), new byte[]{1, 2, 3});
        return new UrlResource(file.toURI());
    }

    @Nested
    @DisplayName("POST /api/files — 파일 업로드")
    class Upload {

        @Test
        @DisplayName("정상 업로드 — 201 Created + Location 헤더")
        void uploadSuccess() throws Exception {
            MockMultipartFile multipartFile = new MockMultipartFile(
                    "multipartFiles",
                    "test.jpg",
                    MediaType.IMAGE_JPEG_VALUE,
                    new byte[]{1, 2, 3}
            );
            given(attachFilePersistenceAdapter.uploadFiles(List.of(multipartFile)))
                    .willReturn(List.of(new FileResponse(1L, "test.jpg")));

            mockMvc.perform(multipart("/api/files")
                            .file(multipartFile))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/api/files/1"))
                    .andExpect(jsonPath("$[0].attachFileId").value(1));
        }
    }

    @Nested
    @DisplayName("GET /api/files/{fileId} — 원본 다운로드")
    class Download {

        @Test
        @DisplayName("정상 다운로드 — 200 OK + Content-Disposition 헤더")
        void downloadSuccess() throws Exception {
            UrlResource resource = createTempFileResource("test.jpg");
            FileDownloadDto dto = new FileDownloadDto(resource, "attachment; filename=\"test.jpg\"");
            given(attachFilePersistenceAdapter.downloadById(1L)).willReturn(dto);

            mockMvc.perform(get("/api/files/1"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.jpg\""));
        }

        @Test
        @DisplayName("존재하지 않는 파일 — 404")
        void downloadNotFound() throws Exception {
            given(attachFilePersistenceAdapter.downloadById(999L))
                    .willThrow(new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));

            mockMvc.perform(get("/api/files/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/files/{fileId}/thumbnail — 썸네일 다운로드")
    class DownloadThumbnail {

        @Test
        @DisplayName("정상 썸네일 다운로드 — 200 OK + Content-Disposition 헤더")
        void thumbnailSuccess() throws Exception {
            UrlResource resource = createTempFileResource("thumb_test.jpg");
            FileDownloadDto dto = new FileDownloadDto(resource, "attachment; filename=\"test.jpg\"");
            given(attachFilePersistenceAdapter.downloadThumbnailById(1L)).willReturn(dto);

            mockMvc.perform(get("/api/files/1/thumbnail"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.jpg\""));
        }

        @Test
        @DisplayName("존재하지 않는 파일 — 404")
        void thumbnailNotFound() throws Exception {
            given(attachFilePersistenceAdapter.downloadThumbnailById(999L))
                    .willThrow(new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));

            mockMvc.perform(get("/api/files/999/thumbnail"))
                    .andExpect(status().isNotFound());
        }
    }
}
