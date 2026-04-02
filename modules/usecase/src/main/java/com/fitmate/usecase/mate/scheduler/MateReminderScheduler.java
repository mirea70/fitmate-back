package com.fitmate.usecase.mate.scheduler;

import com.fitmate.domain.mate.Mate;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.notice.NoticeType;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.notice.LoadNoticePort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MateReminderScheduler {

    private final LoadMatePort loadMatePort;
    private final LoadNoticePort loadNoticePort;
    private static final String REMINDER_MSG = " 모임이 내일 예정되어 있습니다.";

    @Scheduled(cron = "0 0 9 * * *")
    public void sendMateReminder() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime from = tomorrow.atStartOfDay();
        LocalDateTime to = tomorrow.atTime(LocalTime.MAX);

        List<Mate> mates = loadMatePort.loadMatesByMateAtBetween(from, to);

        for (Mate mate : mates) {
            String content = mate.getTitle() + REMINDER_MSG;
            Set<Long> approvedAccountIds = mate.getApprovedAccountIds();
            if (approvedAccountIds == null) continue;

            for (Long accountId : approvedAccountIds) {
                Notice notice = Notice.of(accountId, mate.getId().getValue(), null, content, NoticeType.MATE_REMINDER);
                loadNoticePort.saveNoticeEntity(notice);
            }
        }
    }
}
