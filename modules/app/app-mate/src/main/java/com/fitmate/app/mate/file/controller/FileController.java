package com.fitmate.app.mate.file.controller;

import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@Tag(name = "99 File", description = "파일 관리 API")
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 다운로드", description = "파일 다운로드 API", hidden = true)
    @GetMapping("/{fileId}")
    public ResponseEntity<UrlResource> download(@PathVariable Long fileId) throws MalformedURLException {
        AttachFileDto.Download downloadDto = fileService.downloadById(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, downloadDto.getContentDisposition())
                .body(downloadDto.getUrlResource());
    }
}
