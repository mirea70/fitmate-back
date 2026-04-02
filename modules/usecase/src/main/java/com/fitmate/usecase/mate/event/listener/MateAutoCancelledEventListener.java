package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.MateAutoCancelledEvent;
import com.fitmate.usecase.mate.event.dto.MateAutoCancelledEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MateAutoCancelledEventListener {

    private final LoadNoticePort loadNoticePort;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(MateAutoCancelledEvent event) {
        MateAutoCancelledEventDto dto = event.getEventDto();

        String content = dto.getTitle() + " 모집 규칙이 변경되어 신청이 자동 취소되었습니다.";
        if (dto.getCancelReason() != null && !dto.getCancelReason().isBlank()) {
            content += " (사유 : " + dto.getCancelReason() + ")";
        }

        Notice notice = Notice.of(dto.getApplierId(), dto.getMateId(), dto.getWriterId(), content, NoticeType.MATE_CANCELLED);
        loadNoticePort.saveNoticeEntity(notice);
    }
}
