package com.fitmate.app.mating.service;

import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.event.MateRequestEvent;
import com.fitmate.app.mate.mating.service.MatingRequestService;
import com.fitmate.app.mating.helper.MatingAppTestHelper;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.enums.GatherType;
import com.fitmate.domain.mating.mate.domain.repository.MatingRepository;
import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import com.fitmate.domain.mating.request.domain.repository.MateRequestRepository;
import com.fitmate.domain.redis.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.*;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RecordApplicationEvents
public class MatingRequestTest {

    @Autowired
    private MatingRequestService target;

    @Autowired
    private MatingRepository matingRepository;

    @Autowired
    private MateRequestRepository mateRequestRepository;

    @Autowired
    private MatingAppTestHelper matingAppTestHelper;

    @Autowired
    private ApplicationEvents events;

    @Autowired
    private NoticeRepository noticeRepository;

//    @BeforeEach
//    public void saveBeforeMating() {
//        Mating mating = matingAppTestHelper.getTestMating();
//        matingRepository.save(mating);
//    }

    @Test
    public void 메이트신청_알림DB_저장실패시 () throws Exception {
        // given
        
        // when
        
        // then
        // 메이트신청 DB는 저장잘 되었는지 확인
        // 알림 DB는 저장안되었는지 확인
    }
    
    @Test
    public void 메이트신청_문자_전송실패시 () throws Exception {
        // given
        
        // when
        
        // then
        // 메이트신청 DB는 저장잘 되었는지 확인
        // 문자 DB는 저장안되었는지 확인
    }

    @Test
    @Transactional
    public void 메이트신청_성공 () throws Exception {
        // given
        MatingDto.Apply applyDto = MatingDto.Apply.builder()
                .matingId(3L)
                .accountId(1L)
                .comeAnswer("5글자이상답변")
                .build();

        // when
        Long mateRequestId = target.matingRequest(applyDto);
        MateRequest findMateRequest = mateRequestRepository.findById(mateRequestId).orElse(null);
        Mating findMating = matingRepository.findById(applyDto.getMatingId()).orElse(null);
        // then
        assertThat(findMateRequest).isNotNull();
        assertThat(findMateRequest.getComeAnswer()).isEqualTo(applyDto.getComeAnswer());
        int cnt = (int) events.stream(MateRequestEvent.class).count();
        assertThat(cnt).isEqualTo(1);
        assertThat(findMateRequest.getApproveStatus()).isEqualTo(
                findMating.getGatherType() == GatherType.FAST ? MateRequest.ApproveStatus.APPROVE : MateRequest.ApproveStatus.READY
        );
    }
}
