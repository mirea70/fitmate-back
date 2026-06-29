package com.fitmate.adapter.out.persistence.jpa.file.dto;

import org.springframework.core.io.UrlResource;

public record FileDownloadDto(
        UrlResource urlResource,
        String contentDisposition
) {
}
