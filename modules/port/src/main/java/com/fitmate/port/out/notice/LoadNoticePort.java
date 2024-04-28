package com.fitmate.port.out.notice;

import com.fitmate.domain.notice.Notice;
import com.fitmate.domain.account.AccountId;

import java.util.List;

public interface LoadNoticePort {
    void saveNoticeEntity(Notice notice);
    List<NoticeResponse> getNoticesByAccountId(Long accountId);
    void deleteNoticesByAccountId(AccountId id);
}
