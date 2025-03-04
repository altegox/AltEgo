package org.altego.framework.api;

import org.altego.framework.api.request.DefaultRequest;
import org.altego.framework.client.listener.AbstractListener;
import org.altegox.common.log.Log;
import org.altegox.common.utils.Json;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import reactor.core.publisher.SignalType;

public class HttpClient {

    private final WebClient webClient;

    public HttpClient(String baseUrl, String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public <T extends DefaultRequest> Flux<String> post(T baseRequest) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToFlux(String.class)
                .filter(data -> !"[DONE]".equals(data))
                .doOnError(this::handleError)
                .doFinally(this::handleFinally);
    }

    public <T extends DefaultRequest, R> Flux<R> post(T baseRequest, Class<R> responseType) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToFlux(String.class)
                .filter(data -> !"[DONE]".equals(data))
                .doOnError(this::handleError)
                .doFinally(this::handleFinally)
                .map(response -> Json.fromJson(response, responseType));
    }

    public <T extends DefaultRequest, R> Flux<R> post(T baseRequest, Class<R> responseType, AbstractListener<R> listener) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToFlux(String.class)
                .filter(data -> !"[DONE]".equals(data))
//                .doOnNext(data -> listener.onNext(Json.fromJson(data, responseType)))
                .doOnError(listener::onError)
                .doOnComplete(listener::onFinish)
                .map(response -> Json.fromJson(response, responseType));
    }

    public <T extends DefaultRequest> Mono<String> postSync(T baseRequest) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(handleTerminate())
                .doOnError(this::handleError);
    }

    public <T extends DefaultRequest, R> Mono<R> postSync(T baseRequest, Class<R> responseType) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(this::handleError)
                .doFinally(this::handleFinally)
                .map(response -> Json.fromJson(response, responseType));
    }

    public <T extends DefaultRequest, R> void postSync(T baseRequest, Class<R> responseType, AbstractListener<R> listener) {
        webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(listener::onError)
                .doFinally(this::handleFinally)
                .subscribe(response -> listener.onSuccess(Json.fromJson(response, responseType)));
    }

    private void handleError(Throwable error) {
        Log.error("Request error: " + error.getMessage());
    }

    private void handleFinally(SignalType signalType) {
        Log.debug("Finally with signal: " + signalType);
    }

    private Runnable handleTerminate() {
        return () -> Log.debug("Terminate with signal");
    }
}

