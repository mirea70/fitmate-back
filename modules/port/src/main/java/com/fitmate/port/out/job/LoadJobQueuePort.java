package com.fitmate.port.out.job;

import java.util.Collection;

public interface LoadJobQueuePort {

    void enqueueImageResizingJobs(Collection<Long> attachFileIds);
}
