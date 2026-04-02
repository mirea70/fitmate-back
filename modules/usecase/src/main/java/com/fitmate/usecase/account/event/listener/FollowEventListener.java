package com.fitmate.usecase.account.event.listener;

import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.account.event.FollowEvent;
import com.fitmate.usecase.account.event.dto.FollowEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FollowEventListener {

    private final LoadNoticePort loadNoticePort;
    private static final String FOLLOW_MSG = "님이 회원님을 팔로우하였습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(FollowEvent event) {
        FollowEventDto dto = event.getEventDto();
        String content = dto.getFromNickName() + FOLLOW_MSG;
        Notice notice = Notice.of(dto.getTargetAccountId(), null, dto.getFromAccountId(), content, NoticeType.FOLLOWED);
        loadNoticePort.saveNoticeEntity(notice);
    }
}
