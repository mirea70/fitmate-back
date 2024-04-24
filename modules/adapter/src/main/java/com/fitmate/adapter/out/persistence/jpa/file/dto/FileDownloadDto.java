package com.fitmate.adapter.out.persistence.jpa.file.dto;

import lombok.Getter;
import org.springframework.core.io.UrlResource;

@Getter
public class FileDownloadDto {

    private UrlResource urlResource;
    private String contentDisposition;

    public FileDownloadDto(UrlResource urlResource, String contentDisposition) {
        this.urlResource = urlResource;
        this.contentDisposition = contentDisposition;
    }
}
