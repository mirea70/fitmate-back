package com.fitmate.adapter.out.persistence.jpa.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum FileExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif");

    private final String label;

    public static List<String> getExtensions() {
        return Arrays.stream(values())
                .map(extension -> extension.label).toList();
    }
}
