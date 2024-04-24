package com.fitmate.adapter.out.persistence.jpa.file.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FileResponse {
    private Long attachFileId;
    private String uploadFileName;

    public FileResponse(Long attachFileId, String uploadFileName) {
        this.attachFileId = attachFileId;
        this.uploadFileName = uploadFileName;
    }
}
