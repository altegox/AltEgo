package org.altego.framework.client.listener;

public class ChatListener<T> extends AbstractListener<T> {

    @Override
    public void onNext(T response) {
        futureResponse.complete(response);
    }

    @Override
    public void onSuccess(T response) {
        futureResponse.complete(response);
    }

    @Override
    public void onError(Throwable error) {
        futureResponse.completeExceptionally(error);
    }

    @Override
    public T onFinish() {
        return futureResponse.join();
    }

}