package org.rangenx.framework.api.response;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ModelResponse<T> {

    private Flux<T> responseFlux;

    private Mono<T> responseMono;

    public Flux<T> getResponseFlux() {
        return responseFlux;
    }

    public void setResponseFlux(Flux<T> responseFlux) {
        this.responseFlux = responseFlux;
    }

    public Mono<T> getResponseMono() {
        return responseMono;
    }

    public void setResponseMono(Mono<T> responseMono) {
        this.responseMono = responseMono;
    }

}
