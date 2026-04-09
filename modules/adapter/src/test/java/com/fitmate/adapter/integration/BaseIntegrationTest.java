package com.fitmate.adapter.integration;

import com.fitmate.port.out.chat.LoadChatPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.mate.event.listener.*;
import com.fitmate.usecase.account.event.listener.FollowEventListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 시스템 통합 테스트 베이스 클래스.
 * UseCase → Port → Adapter → Repository → H2 DB 전체 흐름을 검증합니다.
 *
 * MongoDB / Redis 의존성은 Mock 처리하여 JPA 기반 핵심 비즈니스 로직에 집중합니다.
 */
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

    // MongoDB/Redis 의존 포트 Mock
    @MockBean protected LoadChatPort loadChatPort;
    @MockBean protected LoadNoticePort loadNoticePort;

    // Redis Repository Mock
    @MockBean protected com.fitmate.adapter.out.persistence.redis.token.RefreshTokenRepository refreshTokenRepository;
    @MockBean protected com.fitmate.adapter.out.persistence.redis.code.repository.ValidateCodeRepository validateCodeRepository;
    @MockBean protected com.fitmate.adapter.out.persistence.redis.notice.repository.NoticeRepository noticeRepository;

    // MongoDB/Redis 의존 이벤트 리스너 Mock (이벤트에서 NPE 방지)
    @MockBean protected MateChatEventListener mateChatEventListener;
    @MockBean protected MateNoticeEventListener mateNoticeEventListener;
    @MockBean protected MateApproveEventListener mateApproveEventListener;
    @MockBean protected MateRequestEventListener mateRequestEventListener;
    @MockBean protected MateCancelledEventListener mateCancelledEventListener;
    @MockBean protected MateAutoCancelledEventListener mateAutoCancelledEventListener;
    @MockBean protected MateClosedEventListener mateClosedEventListener;
    @MockBean protected FollowEventListener followEventListener;
}
