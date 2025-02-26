package org.altego.framework.client;

import org.altego.framework.api.ChatListener;
import org.altego.framework.api.HttpClient;
import org.altego.framework.api.LangModel;
import org.altego.framework.api.request.DefaultRequest;
import org.altego.framework.api.request.Message;
import org.altego.framework.api.response.ChatResponse;
import org.altego.framework.api.response.ModelResponse;
import org.altego.framework.service.ChatService;
import org.altegox.common.log.Log;

import java.util.List;

public abstract class ChatClient<T extends LangModel> implements ChatService<ChatResponse> {

    protected final T model;
    protected final HttpClient httpClient;
    protected final ChatListener<ChatResponse> listener = new ChatListener<>();

    protected ChatClient(T model) {
        this.model = model;
        this.httpClient = new HttpClient(model.getBaseUrl(), model.getApiKey());
    }

    @Override
    public String chat(String message) {
        DefaultRequest request = DefaultRequest.builder()
                .model(model.getModelName())
                .messages(List.of(Message.user(message)))
                .build();
        httpClient.postSync(request, ChatResponse.class, listener);
        ChatResponse chatResponse = listener.onFinish();
        // todo：需处理响应异常的情况
        try {
            return chatResponse.getChoices().getFirst().getMessage().getContent();
        } catch (Exception e) {
            Log.error("Client chat error", e);
        }
        return null;
    }

    @Override
    public ModelResponse<ChatResponse> chat(List<Message> messages) {
        DefaultRequest request = DefaultRequest.builder()
                .model(model.getModelName())
                .stream(model.isStream())
                .messages(messages)
                .build();
        if (model.isStream()) {
            return ModelResponse.of(httpClient.post(request, ChatResponse.class));
        } else {
            httpClient.postSync(request, ChatResponse.class, listener);
            return ModelResponse.of(listener.onFinish());
        }
    }

    @Override
    public <R extends DefaultRequest> ModelResponse<ChatResponse> chat(R request) {
        if (request == null) throw new IllegalArgumentException("request is null");

        if (model.isStream() || request.isStream()) {
            return ModelResponse.of(httpClient.post(request, ChatResponse.class));
        } else {
            httpClient.postSync(request, ChatResponse.class, listener);
            return ModelResponse.of(listener.onFinish());
        }
    }

}