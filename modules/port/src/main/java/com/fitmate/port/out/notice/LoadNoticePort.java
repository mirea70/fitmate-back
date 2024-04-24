package com.fitmate.port.out.notice;

import com.fitmate.domain.account.aggregate.Notice;
import com.fitmate.domain.account.vo.AccountId;

import java.util.List;

public interface LoadNoticePort {
    void saveNoticeEntity(Notice notice);
    List<NoticeResponse> getNoticesByAccountId(Long accountId);
    void deleteNoticesByAccountId(AccountId id);
}
