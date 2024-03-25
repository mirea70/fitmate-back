package com.fitmate.app.mate.listener;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import com.fitmate.app.mate.mating.event.MateApproveEvent;
import com.fitmate.app.mate.mating.event.MateRequestEvent;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import com.fitmate.system.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Set;

@RequiredArgsConstructor
@Configuration
@Component
public class SmsEventListener {

    private final SmsUtil smsUtil;
    private final AccountRepository accountRepository;
    private static final String MATE_REQUEST_MSG = " 모집 글에 메이트 신청이 완료되었습니다.";
    private static final String MATE_APPROVE_MSG = " 모집 글의 메이트 신청에 대한 승인이 완료되었습니다.";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onApplicationEvent(MateRequestEvent event) {

        MateEventDto.Request eventDto = event.getEventDto();
        Account account = accountRepository.findById(eventDto.getAccountId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

        String to = account.getPrivateInfo().getPhone();
        String content = eventDto.getTitle() + MATE_REQUEST_MSG;
        smsUtil.sendOne(to, content);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(MateApproveEvent event) {
        MateEventDto.Approve eventDto = event.getEventDto();
        Set<Long> accountIds = eventDto.getAccountIds();
        accountIds.forEach(accountId -> {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_ACCOUNT_DATA));

            String to = account.getPrivateInfo().getPhone();
            String content = eventDto.getTitle() + MATE_APPROVE_MSG;
            smsUtil.sendOne(to, content);
        });
    }
}
