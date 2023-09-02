package com.fitmate.app.mate.notice.listener;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import com.fitmate.app.mate.mating.event.MateRequestEvent;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.redis.entity.Notice;
import com.fitmate.domain.redis.repository.NoticeRepository;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;


//@EnableAsync
@RequiredArgsConstructor
@Configuration
@Component
public class NoticeEventListener {

    private final NoticeRepository noticeRepository;

    private final MatingRepository matingRepository;

//    @Async
    @EventListener
    public void onApplicationEvent(MateRequestEvent event) {
        MateEventDto.Request eventDto = event.getEventDto();
        Mating findMating = matingRepository.findById(eventDto.getMatingId())
                .orElseThrow(() -> new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA));
        String content = findMating.getTitle() + " 모집 글에 메이트 신청이 완료되었습니다.";
        Notice notice = Notice.builder()
                .accountId(eventDto.getAccountId())
                .content(content)
                .build();
        noticeRepository.save(notice);
    }
}
