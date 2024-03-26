package com.fitmate.app.mate.listener;

import com.fitmate.app.mate.account.event.AccountDeleteEvent;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@Component
public class MateRequestEventListener {

    private final MateRequestRepository mateRequestRepository;

    @EventListener
    public void onApplicationEvent(AccountDeleteEvent event) {
        Long accountId = event.getEventDto().getAccountId();
        mateRequestRepository.deleteAllByAccountId(accountId);
    }
}
