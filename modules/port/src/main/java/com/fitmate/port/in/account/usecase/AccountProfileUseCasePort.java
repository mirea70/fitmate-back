package com.fitmate.port.in.account.usecase;

import com.fitmate.domain.mate.vo.ApproveStatus;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.command.AccountModifyCommand;
import com.fitmate.port.out.account.AccountProfileResponse;
import com.fitmate.port.out.follow.FollowDetailResponse;
import com.fitmate.port.out.mate.dto.MateRequestSimpleResponse;
import com.fitmate.port.out.notice.NoticeResponse;

import java.io.IOException;
import java.util.List;

public interface AccountProfileUseCasePort {
    void join(AccountJoinCommand accountJoinCommand) throws IOException;
    AccountProfileResponse findAccount(Long accountId);
    void modify(AccountModifyCommand accountModifyCommand);
    void delete(Long accountId);
    void followOrCancel(Long fromAccountId, Long toAccountId);
    List<FollowDetailResponse> getFollowingList(Long accountId);
    List<FollowDetailResponse> getFollowerList(Long accountId);
    List<NoticeResponse> getNotices(Long accountId);
    List<MateRequestSimpleResponse> getMyMateRequests(Long applierId, ApproveStatus approveStatus);
}
