package org.rangenx.api;

import org.rangenx.framework.api.ChatListener;
import org.rangenx.framework.api.HttpClient;
import org.rangenx.framework.api.request.DefaultRequest;
import org.rangenx.framework.api.request.Message;
import org.rangenx.framework.api.response.DefaultChatResponse;
import org.rangenx.framework.service.ChatService;
import org.rangenx.model.OpenaiModel;

import java.util.List;

public class OpenaiClient implements ChatService<DefaultChatResponse> {

    private final OpenaiModel openaiModel;
    private final HttpClient httpClient;
    private final ChatListener<DefaultChatResponse> listener = new ChatListener<>();

    private OpenaiClient(OpenaiModel openaiModel) {
        this.httpClient = new HttpClient(openaiModel.getBaseUrl(), openaiModel.getApiKey());
        this.openaiModel = openaiModel;
    }

    public static OpenaiClient create(OpenaiModel openaiModel) {
        return new OpenaiClient(openaiModel);
    }

    @Override
    public String chat(String message) {
        return chat(List.of(Message.user(message))).getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public DefaultChatResponse chat(List<Message> messages) {
        DefaultRequest request = DefaultRequest.builder()
                .model(openaiModel.getModelName())
                .stream(openaiModel.isStream())
                .messages(messages)
                .build();
        httpClient.postSync(request, DefaultChatResponse.class, listener);
        return listener.onFinish();
    }

    @Override
    public <R extends DefaultRequest> DefaultChatResponse chat(R request) {
        return null;
    }

    @Override
    public String completions(String message) {
        return "";
    }

    @Override
    public DefaultChatResponse completions(List<Message> messages) {
        return null;
    }

    @Override
    public <R extends DefaultRequest> DefaultChatResponse completions(R request) {
        return null;
    }

}
