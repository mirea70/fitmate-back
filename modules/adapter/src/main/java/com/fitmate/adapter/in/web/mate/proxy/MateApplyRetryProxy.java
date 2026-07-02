package com.fitmate.adapter.in.web.mate.proxy;

import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MateApplyRetryProxy {

    private final MateApplyUseCasePort mateApplyUseCasePort;

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100),
            listeners = "customRetryListener"
    )
    public void applyMate(MateApplyCommand command) {
        mateApplyUseCasePort.applyMate(command);
    }

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100),
            listeners = "customRetryListener"
    )
    public void approveMate(MateApproveCommand command) {
        mateApplyUseCasePort.approveMate(command);
    }

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100),
            listeners = "customRetryListener"
    )
    public void cancelMateApply(Long mateId, Long applierId, String cancelReason) {
        mateApplyUseCasePort.cancelMateApply(mateId, applierId, cancelReason);
    }

}
