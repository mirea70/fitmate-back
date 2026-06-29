package com.fitmate.adapter.in.web.mate.proxy;

import com.fitmate.adapter.out.persistence.jpa.retry.entity.RetryCountJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryDomain;
import com.fitmate.adapter.out.persistence.jpa.retry.enums.RetryType;
import com.fitmate.adapter.out.persistence.jpa.retry.repository.RetryCountQueryRepository;
import com.fitmate.adapter.out.persistence.jpa.retry.repository.RetryCountRepository;
import com.fitmate.port.in.mate.command.MateApplyCommand;
import com.fitmate.port.in.mate.command.MateApproveCommand;
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
public class CustomRetryListener implements RetryListener {

    private final RetryCountRepository retryCountRepository;
    private final RetryCountQueryRepository retryCountQueryRepository;

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        Object[] args = (Object[]) context.getAttribute("context.args");
        if (args == null || args.length == 0) return;

        RetryTarget retryTarget = extractRetryTarget(args[0]);
        if (retryTarget == null) return;

        log.info("낙관적 락 재시도 발생 — domain: {}, targetId: {}, type: {}, 재시도 횟수: {}",
                retryTarget.domain, retryTarget.targetId, retryTarget.type, context.getRetryCount());
        recordRetry(retryTarget.domain, retryTarget.targetId, retryTarget.type);
    }

    private RetryTarget extractRetryTarget(Object arg) {
        if (arg instanceof MateApplyCommand) {
            return new RetryTarget(RetryDomain.MATE, ((MateApplyCommand) arg).getMateId(), RetryType.APPLY);
        }
        if (arg instanceof MateApproveCommand) {
            return new RetryTarget(RetryDomain.MATE, ((MateApproveCommand) arg).getMateId(), RetryType.APPROVE);
        }
        return null;
    }

    public void recordRetry(RetryDomain domain, Long targetId, RetryType type) {
        if (!retryCountRepository.existsByDomainAndTargetIdAndType(domain, targetId, type)) {
            retryCountRepository.save(new RetryCountJpaEntity(domain, targetId, type));
        }
        retryCountQueryRepository.incrementRetryCount(domain, targetId, type);
    }

    private static class RetryTarget {
        private final RetryDomain domain;
        private final Long targetId;
        private final RetryType type;

        private RetryTarget(RetryDomain domain, Long targetId, RetryType type) {
            this.domain = domain;
            this.targetId = targetId;
            this.type = type;
        }
    }
}
