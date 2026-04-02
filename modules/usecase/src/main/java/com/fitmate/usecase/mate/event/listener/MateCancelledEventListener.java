package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.MateCancelledEvent;
import com.fitmate.usecase.mate.event.dto.MateCancelledEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MateCancelledEventListener {

    private final LoadNoticePort loadNoticePort;
    private final LoadAccountPort loadAccountPort;
    private static final String MATE_CANCELLED_MSG = " 메이트 모집 신청을 취소하였습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(MateCancelledEvent event) {
        MateCancelledEventDto dto = event.getEventDto();
        Account applier = loadAccountPort.loadAccountEntity(new AccountId(dto.getApplierId()));
        String nickName = applier.getProfileInfo().getNickName();

        String content = nickName + "님이 " + dto.getTitle() + MATE_CANCELLED_MSG;
        if (dto.getCancelReason() != null && !dto.getCancelReason().isBlank()) {
            content += " (사유 : " + dto.getCancelReason() + ")";
        }
        Notice notice = Notice.of(dto.getWriterId(), dto.getMateId(), dto.getApplierId(), content, NoticeType.MATE_CANCELLED);
        loadNoticePort.saveNoticeEntity(notice);
    }
}
