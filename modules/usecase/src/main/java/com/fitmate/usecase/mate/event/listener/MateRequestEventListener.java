package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.mate.enums.ApproveStatus;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.MateRequestEvent;
import com.fitmate.usecase.mate.event.dto.MateRequestEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MateRequestEventListener {

    private final LoadNoticePort loadNoticePort;
    private static final String MATE_REQUEST_MSG = " 모집 글에 새로운 메이트 신청이 있습니다.";
    private static final String MATE_APPROVE_MSG = " 모집 글의 메이트 신청이 승인되었습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(MateRequestEvent event) {
        MateRequestEventDto dto = event.getEventDto();

        String requestContent = dto.getTitle() + MATE_REQUEST_MSG;
        Notice requestNotice = Notice.of(dto.getWriterId(), dto.getMateId(), dto.getApplierId(), requestContent, NoticeType.MATE_REQUESTED);
        loadNoticePort.saveNoticeEntity(requestNotice);

        if (dto.getApproveStatus() == ApproveStatus.APPROVE) {
            String approveContent = dto.getTitle() + MATE_APPROVE_MSG;
            Notice approveNotice = Notice.of(dto.getApplierId(), dto.getMateId(), null, approveContent, NoticeType.MATE_APPROVED);
            loadNoticePort.saveNoticeEntity(approveNotice);
        }
    }
}
