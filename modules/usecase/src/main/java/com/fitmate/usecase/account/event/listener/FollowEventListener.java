package com.fitmate.usecase.account.event.listener;

import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.OutboxEventPublisherPort;
import com.fitmate.port.out.outbox.payload.FollowedEventPayload;
import com.fitmate.usecase.account.event.FollowEvent;
import com.fitmate.usecase.account.event.dto.FollowEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FollowEventListener {

    private final OutboxEventPublisherPort outboxEventPublisherPort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(FollowEvent event) {
        FollowEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.FOLLOWED,
                dto.getTargetAccountId(),
                FollowedEventPayload.builder()
                        .fromAccountId(dto.getFromAccountId())
                        .targetAccountId(dto.getTargetAccountId())
                        .fromNickName(dto.getFromNickName())
                        .build()
        );
    }
}
