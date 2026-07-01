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
                dto.mateId(),
                MateRegisteredEventPayload.builder()
                        .mateId(dto.mateId())
                        .writerId(dto.writerId())
                        .title(dto.title())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateModified(MateModifiedEvent event) {
        MateModifiedEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_MODIFIED,
                dto.mateId(),
                MateModifiedEventPayload.builder()
                        .mateId(dto.mateId())
                        .title(dto.title())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateRequested(MateRequestEvent event) {
        MateRequestEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_REQUESTED,
                dto.mateId(),
                MateRequestedEventPayload.builder()
                        .mateId(dto.mateId())
                        .writerId(dto.writerId())
                        .applierId(dto.applierId())
                        .title(dto.title())
                        .approveStatus(dto.approveStatus())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateApproved(MateApproveEvent event) {
        MateApproveEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_APPROVED,
                dto.mateId(),
                MateApprovedEventPayload.builder()
                        .mateId(dto.mateId())
                        .applierId(dto.applierId())
                        .title(dto.title())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateCancelled(MateCancelledEvent event) {
        MateCancelledEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_CANCELLED,
                dto.mateId(),
                MateCancelledEventPayload.builder()
                        .mateId(dto.mateId())
                        .writerId(dto.writerId())
                        .applierId(dto.applierId())
                        .title(dto.title())
                        .cancelReason(dto.cancelReason())
                        .wasApproved(dto.wasApproved())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateAutoCancelled(MateAutoCancelledEvent event) {
        MateAutoCancelledEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_AUTO_CANCELLED,
                dto.mateId(),
                MateAutoCancelledEventPayload.builder()
                        .mateId(dto.mateId())
                        .writerId(dto.writerId())
                        .applierId(dto.applierId())
                        .title(dto.title())
                        .cancelReason(dto.cancelReason())
                        .build()
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMateClosed(MateClosedEvent event) {
        MateClosedEventDto dto = event.getEventDto();
        outboxEventPublisherPort.publish(
                EventType.MATE_CLOSED,
                dto.mateId(),
                MateClosedEventPayload.builder()
                        .mateId(dto.mateId())
                        .writerId(dto.writerId())
                        .title(dto.title())
                        .wisherAccountIds(dto.wisherAccountIds())
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
