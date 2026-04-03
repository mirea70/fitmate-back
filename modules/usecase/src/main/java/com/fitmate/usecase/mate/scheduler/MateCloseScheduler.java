package com.fitmate.usecase.mate.scheduler;

import com.fitmate.domain.mate.Mate;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.mate.LoadMatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MateCloseScheduler {

    private final LoadMatePort loadMatePort;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void closeExpiredMates() {
        LocalDateTime now = LocalDateTime.now();
        List<Loaded<Mate>> unclosedMates = loadMatePort.loadUnclosedMatesBeforeMateAt(now);

        for (Loaded<Mate> loadedMate : unclosedMates) {
            loadedMate.update(Mate::close);
            log.info("모집 자동 마감: {} (mateAt: {})", loadedMate.get().getTitle(), loadedMate.get().getMateAt());
        }

        if (!unclosedMates.isEmpty()) {
            log.info("총 {}건의 모집이 자동 마감되었습니다.", unclosedMates.size());
        }
    }
}
