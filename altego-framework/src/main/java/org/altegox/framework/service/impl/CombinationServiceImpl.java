package org.altegox.framework.service.impl;

import org.altegox.common.log.Log;
import org.altegox.framework.api.HttpClient;
import org.altegox.framework.api.LangModel;
import org.altegox.framework.api.request.DefaultRequest;
import org.altegox.framework.api.request.Message;
import org.altegox.framework.api.response.ChatResponse;
import org.altegox.framework.api.response.ModelResponse;
import org.altegox.framework.service.ChatService;
import org.altegox.framework.service.listener.AbstractListener;
import org.altegox.framework.service.listener.ChatListener;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CombinationServiceImpl implements ChatService<ChatResponse> {
    private final LangModel model;
    private final HttpClient reasonerClient;
    private final HttpClient generateClient;
    private final AbstractListener<ChatResponse> listener = new ChatListener<>();

    public CombinationServiceImpl(LangModel model) {
        this.model = model;
        this.reasonerClient = new HttpClient(
                model.getReasonerModel().getBaseUrl(),
                model.getReasonerModel().getApiKey()
        );
        this.generateClient = new HttpClient(
                model.getGenerateModel().getBaseUrl(),
                model.getGenerateModel().getApiKey()
        );
    }

    @Override
    public String generate(String message) {
        DefaultRequest reasonerRequest = DefaultRequest.builder()
                .model(model.getReasonerModel().getModelName())
                .stream(true) // 必须设置为 true 才返回推理内容
                .messages(List.of(Message.user(message)))
                .build();

        String reasonerContent = Objects.requireNonNull(reasonerClient.post(reasonerRequest, ChatResponse.class)
                        .map(response -> Optional.ofNullable(response)
                                .map(ChatResponse::getChoices)
                                .filter(choices -> !choices.isEmpty())
                                .map(choices -> choices.getFirst().getDelta())
                                .map(ChatResponse.Delta::getReasoningContent)
                                .orElse("")
                        )
                        .reduce(new StringBuilder(), StringBuilder::append)
                        .block())
                .toString();

        reasonerContent = "<think>" + reasonerContent + "</think>";
        Log.debug("Reasoner response: {}", reasonerContent);

        // 构造生成模型请求
        DefaultRequest generateRequest = DefaultRequest.builder()
                .model(model.getGenerateModel().getModelName())
                .messages(List.of(Message.user(reasonerContent + "\n" + message)))
                .build();

        generateClient.postSync(generateRequest, ChatResponse.class, listener);

        return Optional.ofNullable(listener.onFinish())
                .map(ChatResponse::getChoices)
                .filter(choices -> !choices.isEmpty())
                .map(choices -> choices.getFirst().getMessage().getContent())
                .orElse("");
    }


    @Override
    public ModelResponse<ChatResponse> chat(List<Message> messages) {
        boolean isStream = model.isStream();

        // Reasoner 请求，永远以流式拿到推理内容
        DefaultRequest reasonerRequest = DefaultRequest.builder()
                .model(model.getReasonerModel().getModelName())
                .stream(true) // 这里的推理部分仍然是流式的
                .messages(messages)
                .build();

        StringBuilder reasoningContentBuffer = new StringBuilder("<think>");

        if (isStream) {
            Flux<ChatResponse> reasonerStream = reasonerClient.post(reasonerRequest, ChatResponse.class)
                    .takeWhile(response -> {
                        String rc = Optional.ofNullable(response)
                                .map(ChatResponse::getChoices)
                                .filter(choices -> !choices.isEmpty())
                                .map(choices -> choices.getFirst().getDelta())
                                .map(ChatResponse.Delta::getReasoningContent)
                                .orElse(null);

                        if (rc == null && (response.getChoices().getFirst().getDelta().getContent() != null
                                || response.getChoices().getFirst().getMessage().getContent() != null)) {
                            reasoningContentBuffer.append("</think>\n");
                            return false;
                        } else {
                            reasoningContentBuffer.append(rc);
                            return true;
                        }
                    });

            Flux<ChatResponse> generatorStream = Flux.defer(() -> {
                String originContent = messages.getLast().getContent();
                String newContent = reasoningContentBuffer + originContent;
                messages.getLast().setContent(newContent);

                DefaultRequest generateRequest = DefaultRequest.builder()
                        .model(model.getGenerateModel().getModelName())
                        .stream(true)
                        .messages(messages)
                        .build();

                return generateClient.post(generateRequest, ChatResponse.class);
            });

            return ModelResponse.of(reasonerStream.concatWith(generatorStream));
        } else {
            // 非流式处理
            reasonerClient.postSync(reasonerRequest, ChatResponse.class, listener);
            ChatResponse reasonerResponse = listener.onFinish();
            String reasoningContent = Optional.ofNullable(reasonerResponse)
                    .map(ChatResponse::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.getFirst().getMessage().getContent())
                    .orElse("");

            reasoningContent = "<think>" + reasoningContent + "</think>";

            String originContent = messages.getLast().getContent();
            String newContent = reasoningContent + "\n" + originContent;
            messages.getLast().setContent(newContent);

            DefaultRequest generateRequest = DefaultRequest.builder()
                    .model(model.getGenerateModel().getModelName())
                    .messages(messages)
                    .build();

            generateClient.postSync(generateRequest, ChatResponse.class, listener);
            ChatResponse finalResponse = listener.onFinish();

            return ModelResponse.of(finalResponse);
        }
    }

    @Override
    public <R extends DefaultRequest> ModelResponse<ChatResponse> chat(R request) {
        boolean isStream = model.isStream();

        StringBuilder reasoningContentBuffer = new StringBuilder("<think>");

        if (isStream) {
            Flux<ChatResponse> reasonerStream = reasonerClient.post(request, ChatResponse.class)
                    .takeWhile(response -> {
                        String rc = Optional.ofNullable(response)
                                .map(ChatResponse::getChoices)
                                .filter(choices -> !choices.isEmpty())
                                .map(choices -> choices.getFirst().getDelta())
                                .map(ChatResponse.Delta::getReasoningContent)
                                .orElse(null);

                        if (rc == null && (response.getChoices().getFirst().getDelta().getContent() != null
                                || response.getChoices().getFirst().getMessage().getContent() != null)) {
                            reasoningContentBuffer.append("</think>\n");
                            return false;
                        } else {
                            reasoningContentBuffer.append(rc);
                            return true;
                        }
                    });

            Flux<ChatResponse> generatorStream = Flux.defer(() -> {
                List<Message> messages = request.getMessages();
                String originContent = messages.getLast().getContent();
                String newContent = reasoningContentBuffer + originContent;
                messages.getLast().setContent(newContent);

                return generateClient.post(request, ChatResponse.class);
            });

            return ModelResponse.of(reasonerStream.concatWith(generatorStream));
        } else {
            // 非流式处理
            reasonerClient.postSync(request, ChatResponse.class, listener);
            ChatResponse reasonerResponse = listener.onFinish();
            String reasoningContent = Optional.ofNullable(reasonerResponse)
                    .map(ChatResponse::getChoices)
                    .filter(choices -> !choices.isEmpty())
                    .map(choices -> choices.getFirst().getMessage().getContent())
                    .orElse("");

            reasoningContent = "<think>" + reasoningContent + "</think>";

            List<Message> messages = request.getMessages();
            String originContent = messages.getLast().getContent();
            String newContent = reasoningContent + "\n" + originContent;
            messages.getLast().setContent(newContent);

            generateClient.postSync(request, ChatResponse.class, listener);
            ChatResponse finalResponse = listener.onFinish();

            return ModelResponse.of(finalResponse);
        }
    }
}
