package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.MateApproveEvent;
import com.fitmate.usecase.mate.event.dto.MateApproveEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MateApproveEventListener {

    private final LoadNoticePort loadNoticePort;
    private static final String MATE_APPROVE_MSG = " 모집 글의 메이트 신청이 승인되었습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(MateApproveEvent event) {
        MateApproveEventDto dto = event.getEventDto();
        String content = dto.getTitle() + MATE_APPROVE_MSG;
        Notice notice = Notice.of(dto.getApplierId(), dto.getMateId(), null, content, NoticeType.MATE_APPROVED);
        loadNoticePort.saveNoticeEntity(notice);
    }
}
