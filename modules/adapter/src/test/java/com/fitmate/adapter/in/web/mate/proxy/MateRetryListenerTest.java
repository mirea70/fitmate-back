package com.fitmate.adapter.in.web.mate.proxy;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateRetryCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRetryCountRepository;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("MateRetryListener 단위 테스트")
class MateRetryListenerTest {

    @InjectMocks
    private MateRetryListener mateRetryListener;

    @Mock
    private MateRetryCountRepository mateRetryCountRepository;

    @Test
    @DisplayName("MateApplyCommand 재시도 시 해당 mateId의 retryCount 증가")
    void onError_applyCommand_recordsRetry() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApplyCommand command = new MateApplyCommand(10L, 2L, "답변");

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(1);
        given(mateRetryCountRepository.existsById(10L)).willReturn(false);

        mateRetryListener.onError(context, callback, new RuntimeException());

        then(mateRetryCountRepository).should().save(any(MateRetryCountJpaEntity.class));
        then(mateRetryCountRepository).should().incrementRetryCount(10L);
    }

    @Test
    @DisplayName("context.args가 null이면 기록하지 않음")
    void onError_nullArgs_noRecord() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);

        given(context.getAttribute("context.args")).willReturn(null);

        mateRetryListener.onError(context, callback, new RuntimeException());

        then(mateRetryCountRepository).should(never()).incrementRetryCount(any());
    }

    @Test
    @DisplayName("이미 존재하는 mateId면 새로 생성하지 않고 카운트만 증가")
    void onError_existingMate_incrementOnly() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<?, ?> callback = mock(RetryCallback.class);
        MateApplyCommand command = new MateApplyCommand(10L, 2L, "답변");

        given(context.getAttribute("context.args")).willReturn(new Object[]{command});
        given(context.getRetryCount()).willReturn(2);
        given(mateRetryCountRepository.existsById(10L)).willReturn(true);

        mateRetryListener.onError(context, callback, new RuntimeException());

        then(mateRetryCountRepository).should(never()).save(any());
        then(mateRetryCountRepository).should().incrementRetryCount(10L);
    }
}
