package org.rangenx.framework.api;

import org.rangenx.common.Log;
import org.rangenx.common.utils.Json;
import org.rangenx.framework.api.request.DefaultRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HttpClient {

    private final WebClient webClient;

    public HttpClient(String baseurl, String apikey) {
        this.webClient = WebClient.builder()
                .baseUrl(baseurl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apikey)
                .build();
    }

    public <T extends DefaultRequest> Flux<String> post(T baseRequest) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToFlux(String.class)
                .doOnTerminate(handleTerminate())
                .doOnError(this::handleError);
    }

    public <T extends DefaultRequest> Mono<String> postSync(T baseRequest) {
        return webClient.post()
                .bodyValue(Json.toJson(baseRequest))
                .retrieve()
                .bodyToMono(String.class)
                .doOnTerminate(handleTerminate())
                .doOnError(this::handleError);
    }

    private void handleError(Throwable error) {
        Log.error("Request error: " + error.getMessage());
    }

    private Runnable handleTerminate() {
        return () -> Log.debug("Request terminated");
    }

}

