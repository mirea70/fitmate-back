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
@DisplayName("CustomRetryListener unit test")
class CustomRetryListenerTest {

    @InjectMocks
    private CustomRetryListener customRetryListener;

    @Mock
    private RetryCountRepository retryCountRepository;

    @Mock
    private RetryCountQueryRepository retryCountQueryRepository;

    @Test
    @DisplayName("apply retry records MATE/APPLY retryCount")
    void onError_applyCommand_recordsRetry() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApplyCommand command = new MateApplyCommand(10L, 2L, "answer");

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(1);
        given(retryCountRepository.existsByDomainAndTargetIdAndType(RetryDomain.MATE, 10L, RetryType.APPLY)).willReturn(false);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountRepository).should().save(any(RetryCountJpaEntity.class));
        then(retryCountQueryRepository).should().incrementRetryCount(RetryDomain.MATE, 10L, RetryType.APPLY);
    }

    @Test
    @DisplayName("approve retry records MATE/APPROVE retryCount")
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
    @DisplayName("cancel retry records MATE/CANCEL retryCount")
    void onError_cancelArgs_recordsRetry() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);

        given(context.getAttribute("context.args")).willReturn(new Object[]{10L, 2L, "cancel"});
        given(context.getRetryCount()).willReturn(1);
        given(retryCountRepository.existsByDomainAndTargetIdAndType(RetryDomain.MATE, 10L, RetryType.CANCEL)).willReturn(false);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountRepository).should().save(any(RetryCountJpaEntity.class));
        then(retryCountQueryRepository).should().incrementRetryCount(RetryDomain.MATE, 10L, RetryType.CANCEL);
    }

    @Test
    @DisplayName("null args do not record retry")
    void onError_nullArgs_noRecord() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);

        given(context.getAttribute("context.args")).willReturn(null);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountQueryRepository).should(never()).incrementRetryCount(any(), any(), any());
    }

    @Test
    @DisplayName("existing retry target increments only")
    void onError_existingTarget_incrementOnly() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApplyCommand command = new MateApplyCommand(10L, 2L, "answer");

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(2);
        given(retryCountRepository.existsByDomainAndTargetIdAndType(RetryDomain.MATE, 10L, RetryType.APPLY)).willReturn(true);

        customRetryListener.onError(context, callback, new RuntimeException());

        then(retryCountRepository).should(never()).save(any());
        then(retryCountQueryRepository).should().incrementRetryCount(RetryDomain.MATE, 10L, RetryType.APPLY);
    }
}
