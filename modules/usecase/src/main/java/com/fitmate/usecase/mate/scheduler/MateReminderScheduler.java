package com.fitmate.usecase.mate.scheduler;

import com.fitmate.domain.mate.Mate;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.usecase.mate.event.MateReminderEvent;
import com.fitmate.usecase.mate.event.dto.MateReminderEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MateReminderScheduler {

    private final LoadMatePort loadMatePort;
    private final LoadMateRequestPort loadMateRequestPort;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendMateReminder() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime from = tomorrow.atStartOfDay();
        LocalDateTime to = tomorrow.atTime(LocalTime.MAX);

        List<Mate> mates = loadMatePort.loadMatesByMateAtBetween(from, to);

        for (Mate mate : mates) {
            Set<Long> approvedAccountIds = loadMateRequestPort.getApprovedAccountIds(mate.getId().getValue());
            approvedAccountIds.add(mate.getWriterId());
            if (approvedAccountIds.isEmpty()) continue;

            for (Long accountId : approvedAccountIds) {
                eventPublisher.publishEvent(new MateReminderEvent(
                        new MateReminderEventDto(mate.getId().getValue(), accountId, mate.getTitle())
                ));
            }
        }
    }
}
