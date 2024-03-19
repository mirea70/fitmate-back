package com.fitmate.domain.file.repository;

import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.helper.AttachFileDomainTestHelper;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AttachFileDomainTestHelper.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"rdbms", "dev"})
public class AttachFileRepositoryTest {
    @Autowired
    private AttachFileRepository attachFileRepository;
    @Autowired
    private AttachFileDomainTestHelper attachFileDomainTestHelper;

    @Test
    public void AttachFileRepository가Null이아님 () throws Exception {
        assertThat(attachFileRepository).isNotNull();
    }

    @Test
    public void 파일정보저장 () throws Exception {
        // given
        AttachFile attachFile = attachFileDomainTestHelper.getTestFile();
        // when
        final AttachFile result = attachFileRepository.save(attachFile);
        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(result);
    }

    @Test
    public void 파일정보조회 () throws Exception {
        // given
        AttachFile attachFile = attachFileDomainTestHelper.getTestFile();
        final AttachFile savedFile = attachFileRepository.save(attachFile);
        // when
        final AttachFile result = attachFileRepository.findById(attachFile.getId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedFile);
    }

    @Test
    public void 파일삭제 () throws Exception {
        // given
        AttachFile attachFile = attachFileDomainTestHelper.getTestFile();
        final AttachFile savedFile = attachFileRepository.save(attachFile);
        // when
        final AttachFile findFile = attachFileRepository.findById(attachFile.getId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        attachFileRepository.delete(findFile);
        final Optional<AttachFile> result = attachFileRepository.findById(savedFile.getId());
        // then
        assertThat(result).isEmpty();
    }
}
