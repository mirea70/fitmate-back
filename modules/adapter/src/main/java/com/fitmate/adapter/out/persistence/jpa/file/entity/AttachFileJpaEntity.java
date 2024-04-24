package com.fitmate.adapter.out.persistence.jpa.file.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "attach_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AttachFileJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String uploadFileName;
    @Column(nullable = false)
    private String storeFileName;

    public AttachFileJpaEntity(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
