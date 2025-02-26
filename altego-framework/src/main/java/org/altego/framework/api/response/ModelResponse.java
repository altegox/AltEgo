package org.altego.framework.api.response;

import reactor.core.publisher.Flux;

public class ModelResponse<T> {

    private Flux<T> stream;

    private T response;

    public Flux<T> stream() {
        return stream;
    }

    public T response() {
        return response;
    }

    public static <T> ModelResponse<T> of(Flux<T> stream) {
        ModelResponse<T> modelResponse = new ModelResponse<>();
        modelResponse.stream = stream;
        return modelResponse;
    }

    public static <T> ModelResponse<T> of(T response) {
        ModelResponse<T> modelResponse = new ModelResponse<>();
        modelResponse.response = response;
        return modelResponse;
    }

}
