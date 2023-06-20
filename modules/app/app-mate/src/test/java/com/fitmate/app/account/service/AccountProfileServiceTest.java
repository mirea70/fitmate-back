package com.fitmate.app.account.service;

import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.account.service.AccountProfileService;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.repository.AttachFileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@Import(AttachFileRepository.class)
public class AccountProfileServiceTest {
    @InjectMocks
    AccountProfileService target;
    @Mock
    AccountRepository accountRepository;
    @Mock
    AttachFileRepository attachFileRepository;
    @Mock
    FileService fileService;
    private final FileTestHelper fileTestHelper = new FileTestHelper();
    private final AccountAppTestHelper accountAppTestHelper = new AccountAppTestHelper();


    @Test
    public void 회원프로필다운로드테스트_성공 () throws Exception {
        // given
        Long accountId = 1L;
        Account account = accountAppTestHelper.getTestAccount();
        AttachFile attachFile = fileTestHelper.getTestAttachFile();
        AttachFileDto.Download fileResponse = fileTestHelper.getTestDownloadDto(attachFile.getUploadFileName(), attachFile.getStoreFileName());

        doReturn(Optional.of(attachFile)).when(attachFileRepository).findById(anyLong());
        doReturn(Optional.of(account)).when(accountRepository).findById(anyLong());
        doReturn(fileResponse).when(fileService).downloadFile(anyString());
        // when
        AttachFileDto.Download result = target.downloadProfileImage(accountId);
        // then
        String path = fileTestHelper.getFullPath(attachFile.getStoreFileName());
        assertThat(result.getUrlResource().getURL().toString()).isEqualTo("file:" + path);
        assertThat(result.getContentDisposition()).isEqualTo("attachment; filename=\"" + attachFile.getUploadFileName() + "\"");
    }


}
