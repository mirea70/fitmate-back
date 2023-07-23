package com.fitmate.domain.redis.helper;

import com.fitmate.domain.redis.entity.Notice;
import org.springframework.stereotype.Component;

@Component
public class NoticeDomainTestHelper {

    public Notice getTestNotice() {
        return Notice.builder()
                .accountId(1L)
                .matingId(1L)
                .content("테스트알림")
                .build();
    }
}
