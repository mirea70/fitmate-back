package com.fitmate.domain.file.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id","storeFileName", "path"})
public class AttachFile {
    @Id
    @Column(name = "file_id")
    private Long id;

    private String uploadFileName;

    private String storeFileName;

    private String path;

    @Builder
    public AttachFile(Long id, String uploadFileName, String storeFileName, String path) {
        this.id = id;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.path = path;
    }
}
