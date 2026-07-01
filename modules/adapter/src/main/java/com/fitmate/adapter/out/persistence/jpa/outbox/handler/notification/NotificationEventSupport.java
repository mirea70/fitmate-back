package com.fitmate.adapter.out.persistence.jpa.outbox.handler.notification;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.follow.LoadFollowPort;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NotificationEventSupport {

    private final LoadNoticePort loadNoticePort;
    private final LoadAccountPort loadAccountPort;
    private final LoadMateWishPort loadMateWishPort;
    private final LoadFollowPort loadFollowPort;

    public void saveNotice(Long targetAccountId, Long mateId, Long fromAccountId, String content, NoticeType noticeType) {
        loadNoticePort.saveNoticeEntity(Notice.of(targetAccountId, mateId, fromAccountId, content, noticeType));
    }

    public String nickName(Long accountId) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(accountId));
        return account.getProfileInfo().getNickName();
    }

    public Set<Long> followerIds(Long accountId) {
        return loadFollowPort.getFollowerIds(accountId);
    }

    public List<Long> wisherAccountIds(Long mateId) {
        return loadMateWishPort.getWisherAccountIds(mateId);
    }
}
