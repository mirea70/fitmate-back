package com.fitmate.adapter.out.persistence.redis.notice.adapter;

import com.fitmate.adapter.PersistenceAdapter;
import com.fitmate.adapter.out.persistence.redis.notice.mapper.NoticePersistenceMapper;
import com.fitmate.adapter.out.persistence.redis.notice.entity.NoticeRedisEntity;
import com.fitmate.adapter.out.persistence.redis.notice.repository.NoticeRepository;
import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.account.AccountId;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.port.out.notice.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class NoticePersistenceAdapter implements LoadNoticePort {

    private final NoticeRepository noticeRepository;
    private final NoticePersistenceMapper noticePersistenceMapper;

    @Override
    public void saveNoticeEntity(Notice notice) {
        NoticeRedisEntity noticeEntity = noticePersistenceMapper.domainToEntity(notice);
        noticeRepository.save(noticeEntity);
    }

    @Override
    public List<NoticeResponse> getNoticesByAccountId(Long accountId) {
        List<NoticeRedisEntity> notices = noticeRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId);
        return noticePersistenceMapper.entitiesToResponses(notices);
    }

    @Override
    public void deleteNoticesByAccountId(AccountId id) {
        noticeRepository.deleteAllByAccountId(id.getValue());
    }
}
