package com.fitmate.domain.redis.repository;

import com.fitmate.domain.redis.config.RedisConfig;
import com.fitmate.domain.redis.entity.Notice;
import com.fitmate.domain.redis.helper.NoticeDomainTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataRedisTest
@Import({RedisConfig.class, NoticeDomainTestHelper.class})
@ActiveProfiles("redis")
public class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private NoticeDomainTestHelper noticeDomainTestHelper;

//    @AfterEach
//    public void 테스트후처리() {
//        noticeRepository.deleteAll();
//    }

    @Test
    public void 알림_저장 () throws Exception {
        // given
        Notice newNotice = noticeDomainTestHelper.getTestNotice();
        // when
        Notice savedNotice = noticeRepository.save(newNotice);
        // then
        assertEquals(savedNotice.getAccountId(), newNotice.getAccountId());
    }
    
    @Test
    public void 알림_조회 () throws Exception {
        // given
        Notice newNotice = noticeDomainTestHelper.getTestNotice();
        Notice savedNotice = noticeRepository.save(newNotice);
        // when
        Optional<Notice> findNotice = noticeRepository.findById(savedNotice.getAccountId());
        // then
        assertTrue(findNotice.isPresent());
        assertThat(findNotice.get().getAccountId()).isEqualTo(savedNotice.getAccountId());
    }
}
