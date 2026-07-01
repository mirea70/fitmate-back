package com.fitmate.adapter.integration;

import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.autoconfigure.exclude=" +
                        "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
        }
)
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @MockBean protected LoadChatPort loadChatPort;
    @MockBean protected LoadNoticePort loadNoticePort;

    @MockBean protected com.fitmate.adapter.out.persistence.redis.token.RefreshTokenRepository refreshTokenRepository;
    @MockBean protected com.fitmate.adapter.out.persistence.redis.code.repository.ValidateCodeRepository validateCodeRepository;
    @MockBean protected com.fitmate.adapter.out.persistence.redis.notice.repository.NoticeRepository noticeRepository;
}
