package com.fitmate.port.out.common;

import java.util.function.Consumer;

public class Loaded<D> {
    private final D domain;
    private final Consumer<D> syncCallback;

    public Loaded(D domain, Consumer<D> syncCallback) {
        this.domain = domain;
        this.syncCallback = syncCallback;
    }

    public D get() {
        return domain;
    }

    public void update(Consumer<D> updater) {
        updater.accept(domain);
        syncCallback.accept(domain);
    }
}
