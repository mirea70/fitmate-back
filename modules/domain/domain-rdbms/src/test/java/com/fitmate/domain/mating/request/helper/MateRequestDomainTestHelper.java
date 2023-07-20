package com.fitmate.domain.mating.request.helper;

import com.fitmate.domain.mating.request.domain.entity.MateRequest;
import org.springframework.stereotype.Component;

@Component
public class MateRequestDomainTestHelper {
    public MateRequest getTestMateRequest() {
        return MateRequest.builder()
                .comeAnswer("대답입니다1")
                .matingId(1L)
                .approveStatus(MateRequest.ApproveStatus.READY)
                .build();
    }
}
