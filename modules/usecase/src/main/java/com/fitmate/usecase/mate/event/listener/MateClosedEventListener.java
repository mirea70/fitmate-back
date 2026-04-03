package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.MateClosedEvent;
import com.fitmate.usecase.mate.event.dto.MateClosedEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MateClosedEventListener {

    private final LoadNoticePort loadNoticePort;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(MateClosedEvent event) {
        MateClosedEventDto dto = event.getEventDto();

        String content = dto.getTitle() + " 모임이 마감되었습니다.";

        for (Long wisherAccountId : dto.getWisherAccountIds()) {
            Notice notice = Notice.of(wisherAccountId, dto.getMateId(), dto.getWriterId(), content, NoticeType.MATE_CANCELLED);
            loadNoticePort.saveNoticeEntity(notice);
        }
    }
}
