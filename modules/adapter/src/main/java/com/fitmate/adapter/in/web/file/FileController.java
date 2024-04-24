package com.fitmate.adapter.in.web.file;

import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileDownloadDto;
import com.fitmate.adapter.out.persistence.jpa.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@Tag(name = "99. File", description = "파일 관리 API")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final AttachFilePersistenceAdapter attachFilePersistenceAdapter;

    @Operation(summary = "파일 업로드", description = "파일 업로드 API")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileResponse>> uploads(@Parameter(description = "업로드할 파일(여러개 가능)")
                                                    @RequestPart(required = false) List<MultipartFile> multipartFiles) throws Exception {
           List<FileResponse> responses = attachFilePersistenceAdapter.uploadFiles(multipartFiles);
           return ResponseEntity.ok(responses);
    }

    @Operation(summary = "파일 다운로드", description = "파일 다운로드 API")
    @GetMapping(path = "/{fileId}")
    public ResponseEntity<UrlResource> download(@PathVariable Long fileId) throws MalformedURLException {
        FileDownloadDto downloadDto = attachFilePersistenceAdapter.downloadById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, downloadDto.getContentDisposition())
                .body(downloadDto.getUrlResource());
    }
}
