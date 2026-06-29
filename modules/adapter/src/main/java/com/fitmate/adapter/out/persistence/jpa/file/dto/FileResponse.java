package com.fitmate.adapter.out.persistence.jpa.file.dto;

public record FileResponse(
        Long attachFileId,
        String uploadFileName
) {
}
