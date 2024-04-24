package com.fitmate.usecase.account.event.listener;

import com.fitmate.domain.account.aggregate.Notice;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.account.event.FollowCancelEvent;
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
    private static final String FOLLOW_MSG = "님을 팔로우하였습니다.";
    private static final String FOLLOW_CANCEL_MSG = "님을 팔로우 취소하였습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(FollowEvent event) {
        FollowEventDto eventDto = event.getEventDto();

        String content = eventDto.getTargetNickName() + FOLLOW_MSG;
        Notice notice = Notice.withOutMatingId(eventDto.getFromAccountId(), content);
        loadNoticePort.saveNoticeEntity(notice);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(FollowCancelEvent event) {
        FollowEventDto eventDto = event.getEventDto();

        String content = eventDto.getTargetNickName() + FOLLOW_CANCEL_MSG;
        Notice notice = Notice.withOutMatingId(eventDto.getFromAccountId(), content);
        loadNoticePort.saveNoticeEntity(notice);
    }
}
