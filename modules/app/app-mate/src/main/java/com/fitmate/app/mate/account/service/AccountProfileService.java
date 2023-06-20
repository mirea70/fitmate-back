package com.fitmate.app.mate.account.service;

import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.repository.AttachFileRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountProfileService {
    private final FileService fileService;
    private final AccountRepository accountRepository;
    private final AttachFileRepository attachFileRepository;

    public AttachFileDto.Download downloadProfileImage(Long accountId) throws MalformedURLException {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        AttachFile attachFile = attachFileRepository.findById(account.getProfileInfo().getProfileImageId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        return fileService.downloadFile(attachFile.getStoreFileName());
    }
}
