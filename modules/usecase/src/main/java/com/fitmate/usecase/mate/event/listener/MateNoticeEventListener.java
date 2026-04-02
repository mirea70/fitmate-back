package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.follow.LoadFollowPort;
import com.fitmate.port.out.mate.LoadMateWishPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.MateModifiedEvent;
import com.fitmate.usecase.mate.event.MateRegisteredEvent;
import com.fitmate.usecase.mate.event.dto.MateModifiedEventDto;
import com.fitmate.usecase.mate.event.dto.MateRegisteredEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MateNoticeEventListener {

    private final LoadNoticePort loadNoticePort;
    private final LoadAccountPort loadAccountPort;
    private final LoadMateWishPort loadMateWishPort;
    private final LoadFollowPort loadFollowPort;
    private static final String MATE_MODIFIED_MSG = " 모집 글의 정보가 수정되었습니다.";
    private static final String FOLLOWER_MATE_REGISTERED_MSG = " 새로운 메이트 모집을 시작하였습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateModified(MateModifiedEvent event) {
        MateModifiedEventDto dto = event.getEventDto();
        String content = dto.getTitle() + MATE_MODIFIED_MSG;

        List<Long> wisherIds = loadMateWishPort.getWisherAccountIds(dto.getMateId());
        for (Long wisherId : wisherIds) {
            Notice notice = Notice.of(wisherId, dto.getMateId(), null, content, NoticeType.MATE_MODIFIED);
            loadNoticePort.saveNoticeEntity(notice);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateRegisteredForFollowers(MateRegisteredEvent event) {
        MateRegisteredEventDto dto = event.getEventDto();

        Account writer = loadAccountPort.loadAccountEntity(new AccountId(dto.getWriterId()));
        String writerNickName = writer.getProfileInfo().getNickName();

        Set<Long> followerIds = loadFollowPort.getFollowerIds(dto.getWriterId());
        for (Long followerId : followerIds) {
            String content = writerNickName + "님이 " + dto.getTitle() + FOLLOWER_MATE_REGISTERED_MSG;
            Notice notice = Notice.of(followerId, dto.getMateId(), dto.getWriterId(), content, NoticeType.MATE_REGISTERED);
            loadNoticePort.saveNoticeEntity(notice);
        }
    }
}
