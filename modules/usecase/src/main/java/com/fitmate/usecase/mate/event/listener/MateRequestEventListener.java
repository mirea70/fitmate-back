package com.fitmate.usecase.mate.event.listener;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.account.AccountId;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.port.out.sms.LoadSmsPort;
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
    private final LoadSmsPort loadSmsPort;
    private final LoadAccountPort loadAccountPort;
    private static final String MATE_REQUEST_MSG = " 모집 글에 메이트 신청이 완료되었습니다.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(MateRequestEvent event) {
        MateRequestEventDto eventDto = event.getEventDto();
        String content = eventDto.getTitle() + MATE_REQUEST_MSG;

        sendNotice(eventDto, content);
        sendSms(eventDto, content);
    }

    private void sendNotice(MateRequestEventDto eventDto, String content) {
        Notice notice = Notice.withMateId(
                eventDto.getApplierId(),
                eventDto.getMateId(),
                content
        );
        loadNoticePort.saveNoticeEntity(notice);
    }

    private void sendSms(MateRequestEventDto eventDto, String content) {
        Account account = loadAccountPort.loadAccountEntity(new AccountId(eventDto.getApplierId()));

        String to = account.getPrivateInfo().getPhone();
        loadSmsPort.sendMessageOne(to, content);
    }
}
