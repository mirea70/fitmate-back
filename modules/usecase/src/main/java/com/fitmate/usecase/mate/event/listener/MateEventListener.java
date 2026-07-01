package com.fitmate.usecase.mate.event.listener;

import com.fitmate.port.out.outbox.EventType;
import com.fitmate.port.out.outbox.OutboxEventPublisherPort;
import com.fitmate.port.out.outbox.payload.*;
import com.fitmate.usecase.mate.event.*;
import com.fitmate.usecase.mate.event.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MateEventListener {

    private final OutboxEventPublisherPort outboxEventPublisherPort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateRegistered(MateRegisteredEvent event) {
        MateRegisteredEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_REGISTERED,
                dto.getMateId(),
                MateRegisteredEventPayload.builder()
                        .mateId(dto.getMateId())
                        .writerId(dto.getWriterId())
                        .title(dto.getTitle())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateModified(MateModifiedEvent event) {
        MateModifiedEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_MODIFIED,
                dto.getMateId(),
                MateModifiedEventPayload.builder()
                        .mateId(dto.getMateId())
                        .title(dto.getTitle())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateRequested(MateRequestEvent event) {
        MateRequestEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_REQUESTED,
                dto.getMateId(),
                MateRequestedEventPayload.builder()
                        .mateId(dto.getMateId())
                        .writerId(dto.getWriterId())
                        .applierId(dto.getApplierId())
                        .title(dto.getTitle())
                        .approveStatus(dto.getApproveStatus())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateApproved(MateApproveEvent event) {
        MateApproveEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_APPROVED,
                dto.getMateId(),
                MateApprovedEventPayload.builder()
                        .mateId(dto.getMateId())
                        .applierId(dto.getApplierId())
                        .title(dto.getTitle())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateCancelled(MateCancelledEvent event) {
        MateCancelledEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_CANCELLED,
                dto.getMateId(),
                MateCancelledEventPayload.builder()
                        .mateId(dto.getMateId())
                        .writerId(dto.getWriterId())
                        .applierId(dto.getApplierId())
                        .title(dto.getTitle())
                        .cancelReason(dto.getCancelReason())
                        .wasApproved(dto.isWasApproved())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateAutoCancelled(MateAutoCancelledEvent event) {
        MateAutoCancelledEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_AUTO_CANCELLED,
                dto.getMateId(),
                MateAutoCancelledEventPayload.builder()
                        .mateId(dto.getMateId())
                        .writerId(dto.getWriterId())
                        .applierId(dto.getApplierId())
                        .title(dto.getTitle())
                        .cancelReason(dto.getCancelReason())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateClosed(MateClosedEvent event) {
        MateClosedEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_CLOSED,
                dto.getMateId(),
                MateClosedEventPayload.builder()
                        .mateId(dto.getMateId())
                        .writerId(dto.getWriterId())
                        .title(dto.getTitle())
                        .wisherAccountIds(dto.getWisherAccountIds())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateReminder(MateReminderEvent event) {
        MateReminderEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_REMINDER,
                dto.getMateId(),
                MateReminderEventPayload.builder()
                        .mateId(dto.getMateId())
                        .accountId(dto.getAccountId())
                        .title(dto.getTitle())
                        .build()
        );
    }
}
