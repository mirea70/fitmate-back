package com.fitmate.app.mate.notice.listener;

import com.fitmate.app.mate.mating.dto.MateEventDto;
import com.fitmate.app.mate.mating.event.MateApproveEvent;
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
    private static final String MATE_REQUEST_MSG = " 모집 글에 메이트 신청이 완료되었습니다.";
    private static final String MATE_APPROVE_MSG = " 모집 글의 메이트 신청에 대한 승인이 완료되었습니다.";

//    @Async
    @EventListener
    public void onApplicationEvent(MateRequestEvent event) {
        MateEventDto.Request eventDto = event.getEventDto();

        String content = eventDto.getTitle() + MATE_REQUEST_MSG;
        Notice notice = Notice.builder()
                .accountId(eventDto.getAccountId())
                .content(content)
                .build();
        noticeRepository.save(notice);
    }

    @EventListener
    public void onApplicationEvent(MateApproveEvent event) {
        MateEventDto.Approve eventDto = event.getEventDto();
        String content = eventDto.getTitle() + MATE_APPROVE_MSG;

        eventDto.getAccountIds().forEach(accountId -> {
            Notice notice = Notice.builder()
                    .accountId(accountId)
                    .content(content)
                    .build();
            noticeRepository.save(notice);
        });
    }
}
