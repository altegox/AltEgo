package org.rangenx.framework.api;

import java.util.concurrent.CompletableFuture;

public class ChatListener<T> {

    private final CompletableFuture<T> futureResponse = new CompletableFuture<>();

    public void onSuccess(T response) {
        futureResponse.complete(response);
    }

    public void onError(Throwable error) {
        futureResponse.completeExceptionally(error);
    }

    public T onFinish() {
        return futureResponse.join();
    }
}