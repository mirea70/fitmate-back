package com.fitmate.port.out.file;


import java.util.Set;

public interface LoadAttachFilePort {
    void checkExistFile(Long fileId);
    void checkExistFiles(Set<Long> fileIds);
}
