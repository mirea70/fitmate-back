package com.fitmate.app.mate.listener;

import com.fitmate.app.mate.account.event.AccountDeleteEvent;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@Component
public class MatingEventListener {

    private final MatingRepository matingRepository;

    @EventListener
    public void onApplicationEvent(AccountDeleteEvent event) {
        Long accountId = event.getEventDto().getAccountId();
        matingRepository.deleteAllByWriterId(accountId);
    }
}
