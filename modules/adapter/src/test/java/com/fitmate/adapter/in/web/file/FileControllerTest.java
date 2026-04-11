package com.fitmate.adapter.in.web.file;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.BDDMockito.given;
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
    @DisplayName("GET /api/file/{fileId} — 원본 다운로드")
    class Download {

        @Test
        @DisplayName("정상 다운로드 — 200 OK + Content-Disposition 헤더")
        void downloadSuccess() throws Exception {
            UrlResource resource = createTempFileResource("test.jpg");
            FileDownloadDto dto = new FileDownloadDto(resource, "attachment; filename=\"test.jpg\"");
            given(attachFilePersistenceAdapter.downloadById(1L)).willReturn(dto);

            mockMvc.perform(get("/api/file/1"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.jpg\""));
        }

        @Test
        @DisplayName("존재하지 않는 파일 — 404")
        void downloadNotFound() throws Exception {
            given(attachFilePersistenceAdapter.downloadById(999L))
                    .willThrow(new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));

            mockMvc.perform(get("/api/file/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/file/{fileId}/thumbnail — 썸네일 다운로드")
    class DownloadThumbnail {

        @Test
        @DisplayName("정상 썸네일 다운로드 — 200 OK + Content-Disposition 헤더")
        void thumbnailSuccess() throws Exception {
            UrlResource resource = createTempFileResource("thumb_test.jpg");
            FileDownloadDto dto = new FileDownloadDto(resource, "attachment; filename=\"test.jpg\"");
            given(attachFilePersistenceAdapter.downloadThumbnailById(1L)).willReturn(dto);

            mockMvc.perform(get("/api/file/1/thumbnail"))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.jpg\""));
        }

        @Test
        @DisplayName("존재하지 않는 파일 — 404")
        void thumbnailNotFound() throws Exception {
            given(attachFilePersistenceAdapter.downloadThumbnailById(999L))
                    .willThrow(new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));

            mockMvc.perform(get("/api/file/999/thumbnail"))
                    .andExpect(status().isNotFound());
        }
    }
}
