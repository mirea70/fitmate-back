package com.fitmate.adapter.in.web.mate.proxy;

import com.fitmate.adapter.out.persistence.jpa.retry.entity.RetryCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryDomain;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryType;
import com.fitmate.adapter.out.persistence.jpa.retry.repository.RetryCountQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.retry.repository.RetryCountRepository;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomRetryListener 단위 테스트")
class CustomRetryListenerTest {

    @InjectMocks
    private CustomRetryListener customRetryListener;

    @Mock
    private RetryCountRepository retryCountRepository;

    @Mock
    private RetryCountQueryRepository retryCountQueryRepository;

    @Test
    @DisplayName("MateApplyCommand 재시도 시 MATE/APPLY retryCount 증가")
    void onError_applyCommand_recordsRetry() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApplyCommand command = new MateApplyCommand(10L, 2L, "답변");

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(1);
        given(retryCountRepository.existsByDomainAndTargetIdAndType(RetryDomain.MATE, 10L, RetryType.APPLY)).willReturn(false);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountRepository).should().save(any(RetryCountJpaEntity.class));
        then(retryCountQueryRepository).should().incrementRetryCount(RetryDomain.MATE, 10L, RetryType.APPLY);
    }

    @Test
    @DisplayName("MateApproveCommand 재시도 시 MATE/APPROVE retryCount 증가")
    void onError_approveCommand_recordsRetry() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApproveCommand command = new MateApproveCommand(10L, 2L, 1L);

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(1);
        given(retryCountRepository.existsByDomainAndTargetIdAndType(RetryDomain.MATE, 10L, RetryType.APPROVE)).willReturn(false);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountRepository).should().save(any(RetryCountJpaEntity.class));
        then(retryCountQueryRepository).should().incrementRetryCount(RetryDomain.MATE, 10L, RetryType.APPROVE);
    }

    @Test
    @DisplayName("context.args가 null이면 기록하지 않음")
    void onError_nullArgs_noRecord() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);

        given(context.getAttribute("context.args")).willReturn(null);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountQueryRepository).should(never()).incrementRetryCount(any(), any(), any());
    }

    @Test
    @DisplayName("이미 존재하는 retry target이면 새로 생성하지 않고 카운트만 증가")
    void onError_existingTarget_incrementOnly() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApplyCommand command = new MateApplyCommand(10L, 2L, "답변");

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(2);
        given(retryCountRepository.existsByDomainAndTargetIdAndType(RetryDomain.MATE, 10L, RetryType.APPLY)).willReturn(true);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountRepository).should(never()).save(any());
        then(retryCountQueryRepository).should().incrementRetryCount(RetryDomain.MATE, 10L, RetryType.APPLY);
    }
}
