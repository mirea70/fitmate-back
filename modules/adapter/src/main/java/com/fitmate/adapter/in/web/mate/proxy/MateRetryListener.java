package com.fitmate.adapter.in.web.mate.proxy;

import com.fitmate.adapter.out.persistence.jpa.mate.entity.MateRetryCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.mate.repository.MateRetryCountRepository;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MateRetryListener implements RetryListener {

    private final MateRetryCountRepository mateRetryCountRepository;

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        Object[] args = (Object[]) context.getAttribute("context.args");
        if (args == null || args.length == 0) return;

        Long mateId = extractMateId(args[0]);
        if (mateId == null) return;

        log.info("낙관적 락 재시도 발생 — mateId: {}, 재시도 횟수: {}", mateId, context.getRetryCount());
        recordRetry(mateId);
    }

    private Long extractMateId(Object arg) {
        if (arg instanceof MateApplyCommand) return ((MateApplyCommand) arg).getMateId();
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordRetry(Long mateId) {
        if (!mateRetryCountRepository.existsById(mateId)) {
            mateRetryCountRepository.save(new MateRetryCountJpaEntity(mateId));
        }
        mateRetryCountRepository.incrementRetryCount(mateId);
    }
}
