package org.altego.framework.client.listener;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractListener<T> {

    protected final CompletableFuture<T> futureResponse = new CompletableFuture<>();

    public abstract void onNext(T response);

    public abstract void onSuccess(T response);

    public abstract void onError(Throwable error);

    public abstract T onFinish();
}