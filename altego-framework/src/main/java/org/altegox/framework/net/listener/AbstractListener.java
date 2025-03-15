package org.altegox.framework.net.listener;

public abstract class AbstractListener<T> {

    public abstract void onNext(T response);

    public abstract void onSuccess(T response);

    public abstract void onError(Throwable error);

    public abstract T onFinish();

}