package com.fitmate.app.mate.account.service;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.account.dto.AccountEventDto;
import com.fitmate.app.mate.account.event.AccountDeleteEvent;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.app.mate.file.dto.AttachFileDto;
import com.fitmate.app.mate.file.service.FileService;
import com.fitmate.domain.account.dto.FollowDetailDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountReadRepository;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.file.entity.AttachFile;
import com.fitmate.domain.file.repository.AttachFileRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountProfileService {
    private final FileService fileService;
    private final AccountRepository accountRepository;
    private final AttachFileRepository attachFileRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AccountReadRepository accountReadRepository;

    @Transactional(readOnly = true)
    public AttachFileDto.Download downloadProfileImage(Long accountId) throws MalformedURLException {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        AttachFile attachFile = attachFileRepository.findById(account.getProfileInfo().getProfileImageId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_FILE_DATA));
        return fileService.downloadFile(attachFile.getStoreFileName());
    }

    public AccountDto.Response modifyProfile(AccountDto.UpdateRequest updateRequest) throws Exception {

        Account account = accountRepository.findById(updateRequest.getAccountId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        ProfileInfo updateProfileInfo = modifyProfileImage(updateRequest, account.getProfileInfo().getProfileImageId());
        account.modifyProfile(updateRequest.getPrivateInfo(), updateProfileInfo);

        return AccountDtoMapper.INSTANCE.toResponse(account);
    }

    private ProfileInfo modifyProfileImage(AccountDto.UpdateRequest updateRequest, Long orgFileId) throws Exception {
        if(updateRequest.getProfileImage() == null) return null;

        fileService.deleteFile(orgFileId);
        Long newImageFileId = fileService.uploadFile(updateRequest.getProfileImage()).getId();

        return getProfileInfoSet(updateRequest, newImageFileId);
    }

    private ProfileInfo getProfileInfoSet(AccountDto.UpdateRequest updateRequest, Long newImageFileId) {
        ProfileInfo profileInfo = updateRequest.getProfileInfo();
        if(profileInfo == null)
            profileInfo = ProfileInfo.builder()
                    .profileImageId(newImageFileId)
                    .build();
        return profileInfo;
    }

    /**
     * 처리 이벤트 (아래 이벤트 중 에러 발생 시, 모두 RollBack 된다.)
     * 1. notice 삭제
     * 2. mate_request 삭제
     * 3. mating 삭제
     */
    public void remove(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        AccountEventDto.Delete eventDto = AccountEventDto.Delete.builder()
                                                                .accountId(accountId)
                                                                .build();
        AccountDeleteEvent event = new AccountDeleteEvent(eventDto);
        eventPublisher.publishEvent(event);

        accountRepository.delete(account);
    }

    public void followOrCancel(Long accountId, Long targetId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        Account target = accountRepository.findById(targetId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        if(!account.isFollowing(targetId))
            follow(account, target);
        else
            cancelFollow(account, target);
    }

    private void follow(Account account, Account target) {
        account.addFollowing(target.getId());
        target.addFollower(account.getId());
    }

    private void cancelFollow(Account account, Account target) {
        account.removeFollowing(target.getId());
        target.removeFollower(account.getId());
    }

    @Transactional(readOnly = true)
    public List<FollowDetailDto> getFollowingList(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        return accountReadRepository.findAllInId(account.getFollowingList());
    }

    @Transactional(readOnly = true)
    public List<FollowDetailDto> getFollowerList(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));
        return accountReadRepository.findAllInId(account.getFollowerList());
    }
}
