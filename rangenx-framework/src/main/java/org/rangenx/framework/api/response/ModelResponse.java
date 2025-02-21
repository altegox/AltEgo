package org.rangenx.framework.api.response;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ModelResponse<T> {

    private Flux<T> stream;

    private Mono<T> responseMono;

    public Flux<T> stream() {
        return stream;
    }

    public void stream(Flux<T> stream) {
        this.stream = stream;
    }

    public Mono<T> getResponseMono() {
        return responseMono;
    }

    public void setResponseMono(Mono<T> responseMono) {
        this.responseMono = responseMono;
    }

}
