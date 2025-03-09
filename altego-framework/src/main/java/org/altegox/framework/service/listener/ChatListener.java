package org.altegox.framework.service.listener;

import java.util.concurrent.CompletableFuture;

public class ChatListener<T> extends AbstractListener<T> {

    private CompletableFuture<T> futureResponse = new CompletableFuture<>();

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
        try {
            return futureResponse.join();
        } finally {
            futureResponse = new CompletableFuture<>();
        }
    }

}