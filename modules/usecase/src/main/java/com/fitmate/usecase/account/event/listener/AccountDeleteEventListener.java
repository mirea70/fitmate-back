package com.fitmate.usecase.account.event.listener;

import com.fitmate.domain.account.vo.AccountId;
import com.fitmate.port.out.follow.LoadFollowPort;
import com.fitmate.port.out.mate.LoadMatePort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.usecase.account.event.AccountDeleteEvent;
import com.fitmate.usecase.account.event.dto.AccountDeleteEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDeleteEventListener {

    private final LoadMatePort loadMatePort;
    private final LoadMateRequestPort loadMateRequestPort;
    private final LoadFollowPort loadFollowPort;


    @EventListener
    public void onApplicationEvent(AccountDeleteEvent event) {
        AccountDeleteEventDto eventDto = event.getEventDto();
        AccountId id = new AccountId(eventDto.getAccountId());

        loadMateRequestPort.deleteAllMateRequestByApplier(id);
        loadMatePort.deleteAllMateByWriter(id);
        loadFollowPort.deleteAllFollowByAccountId(id);
    }
}
