package com.fitmate.usecase.account;

import com.fitmate.domain.account.aggregate.Account;
import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.domain.error.results.DuplicatedErrorResult;
import com.fitmate.domain.mate.vo.ApproveStatus;
import com.fitmate.port.in.account.command.AccountCheckCommand;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.command.AccountModifyCommand;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.out.account.AccountProfileResponse;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.file.LoadAttachFilePort;
import com.fitmate.port.out.follow.FollowDetailResponse;
import com.fitmate.port.out.follow.LoadFollowPort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.mate.dto.MateRequestSimpleResponse;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.port.out.notice.NoticeResponse;
import com.fitmate.usecase.UseCase;
import com.fitmate.usecase.account.event.AccountDeleteEvent;
import com.fitmate.usecase.account.event.FollowCancelEvent;
import com.fitmate.usecase.account.event.FollowEvent;
import com.fitmate.usecase.account.event.dto.AccountDeleteEventDto;
import com.fitmate.usecase.account.event.dto.FollowEventDto;
import com.fitmate.usecase.account.mapper.AccountUseCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional
public class AccountProfileUseCase implements AccountProfileUseCasePort {

    private final LoadAccountPort loadAccountPort;
    private final LoadFollowPort loadFollowPort;
    private final LoadAttachFilePort loadAttachFilePort;
    private final LoadNoticePort loadNoticePort;
    private final LoadMateRequestPort loadMateRequestPort;
    private final AccountUseCaseMapper accountUseCaseMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void join(AccountJoinCommand joinCommand) {
        Long profileImageId = joinCommand.getProfileImageId();
        if(profileImageId != null) loadAttachFilePort.checkExistFile(profileImageId);
        checkDuplicated(null, joinCommand);

        Account account = accountUseCaseMapper.commandToDomain(joinCommand);
        loadAccountPort.saveAccountEntity(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountProfileResponse findAccount(Long rawAccountId) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(rawAccountId));
        return accountUseCaseMapper.domainToResponse(account);
    }

    @Override
    public void modify(AccountModifyCommand modifyCommand) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(modifyCommand.getAccountId()));
        checkDuplicated(account.getId().getValue(), modifyCommand);
        Long profileImageId = modifyCommand.getProfileImageId();
        if(profileImageId != null) loadAttachFilePort.checkExistFile(modifyCommand.getProfileImageId());

        account.updateProfileInfo(modifyCommand.getNickName(), modifyCommand.getIntroduction(), modifyCommand.getProfileImageId());
        account.updatePrivateInfo(modifyCommand.getName(), modifyCommand.getPhone(), modifyCommand.getEmail());
        loadAccountPort.saveAccountEntity(account);
    }

    @Override
    public void delete(Long accountId) {
        publishAccountDeleteEvent(accountId);
        loadAccountPort.deleteAccountEntity(new AccountId(accountId));
    }

    private void publishAccountDeleteEvent(Long accountId) {
        AccountDeleteEventDto eventDto = new AccountDeleteEventDto(accountId);
        AccountDeleteEvent event = new AccountDeleteEvent(eventDto);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void followOrCancel(Long fromAccountId, Long targetAccountId) {
        Account from = loadAccountPort.loadAccountEntity(new AccountId(fromAccountId));
        Account target = loadAccountPort.loadAccountEntity(new AccountId(targetAccountId));

        if(!from.isFollowing(targetAccountId))
            follow(from, target);
        else
            cancelFollow(from, target);
    }

    private void follow(Account from, Account target) {
        Long fromId = from.getId().getValue();
        Long targetId = target.getId().getValue();

        from.addFollowing(targetId);
        target.addFollower(fromId);
        loadFollowPort.saveFollowEntity(fromId, targetId);

        FollowEventDto eventDto = new FollowEventDto(fromId, targetId,target.getProfileInfo().getNickName());
        FollowEvent event = new FollowEvent(eventDto);
        eventPublisher.publishEvent(event);
    }

    private void cancelFollow(Account from, Account target) {
        Long fromId = from.getId().getValue();
        Long targetId = target.getId().getValue();

        from.removeFollowing(targetId);
        target.removeFollower(fromId);
        loadFollowPort.deleteFollowEntity(fromId, targetId);

        FollowEventDto eventDto = new FollowEventDto(fromId, targetId,target.getProfileInfo().getNickName());
        FollowCancelEvent event = new FollowCancelEvent(eventDto);
        eventPublisher.publishEvent(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowDetailResponse> getFollowingList(Long accountId) {
        return loadFollowPort.getFollowingsByFrom(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowDetailResponse> getFollowerList(Long accountId) {
        return loadFollowPort.getFollowersByTarget(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoticeResponse> getNotices(Long accountId) {
        return loadNoticePort.getNoticesByAccountId(accountId);
    }

    @Override
    public List<MateRequestSimpleResponse> getMyMateRequests(Long applierId, ApproveStatus approveStatus) {
        return loadMateRequestPort.loadMateRequests(applierId, approveStatus);
    }

    private void checkDuplicated(Long accountId, AccountCheckCommand command) {
        boolean isDuplicated = loadAccountPort.checkDuplicated(accountId, command.getNickName(), command.getName(),
                                                                command.getEmail(), command.getPhone());
        if(isDuplicated)
            throw new DuplicatedException(DuplicatedErrorResult.DUPLICATED_ACCOUNT_JOIN);
    }
}
