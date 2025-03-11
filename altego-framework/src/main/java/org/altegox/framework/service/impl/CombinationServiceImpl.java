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

import java.util.ArrayList;
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
                .messages(List.of(
                        Message.system(model.getReasonerModel().getSystemMessage()),
                        Message.user(message)
                ))
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

        reasonerContent = "<think>\n" + reasonerContent + "</think>\n";
        Log.debug("Reasoner response: {}", reasonerContent);

        // 构造生成模型请求
        DefaultRequest generateRequest = DefaultRequest.builder()
                .model(model.getGenerateModel().getModelName())
                .messages(List.of(
                        Message.system(model.getGenerateModel().getSystemMessage()),
                        Message.user(reasonerContent + "\n" + message)
                ))
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
        List<Message> messageList = new ArrayList<>(messages);
        // 如果第一个消息不是 system，则添加 system 消息
        if (!"system".equals(messageList.getFirst().getRole())) {
            messageList.addFirst(Message.system(model.getReasonerModel().getSystemMessage()));
        }
        // Reasoner 请求，永远以流式拿到推理内容
        DefaultRequest reasonerRequest = DefaultRequest.builder()
                .model(model.getReasonerModel().getModelName())
                .stream(true)
                .messages(messageList)
                .build();

        StringBuilder reasoningContentBuffer = new StringBuilder("<think>\n");

        if (isStream) {
            Flux<ChatResponse> reasonerStream = reasonerClient.post(reasonerRequest, ChatResponse.class)
                    .takeWhile(response -> {
                        String rc = Optional.ofNullable(response)
                                .map(ChatResponse::getChoices)
                                .filter(choices -> !choices.isEmpty())
                                .map(choices -> choices.getFirst().getDelta())
                                .map(ChatResponse.Delta::getReasoningContent)
                                .orElse(null);
                        System.out.println(rc);
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
                String originContent = messageList.getLast().getContent();
                String newContent = reasoningContentBuffer + originContent;
                messageList.getLast().setContent(newContent);

                // 如果第一个消息不是 system，则添加 system 消息
                if (!"system".equals(messageList.getFirst().getRole())) {
                    messageList.addFirst(Message.system(model.getReasonerModel().getSystemMessage()));
                } else {
                    // 如果第一个消息是 system，则为reasonerModel的 system，修改为generatorModel content
                    messageList.getFirst().setContent(model.getGenerateModel().getSystemMessage());
                }

                DefaultRequest generateRequest = DefaultRequest.builder()
                        .model(model.getGenerateModel().getModelName())
                        .stream(true)
                        .messages(messageList)
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

            reasoningContent = "<think>\n" + reasoningContent + "</think>\n";

            String originContent = messageList.getLast().getContent();
            String newContent = reasoningContent + "\n" + originContent;
            messageList.getLast().setContent(newContent);

            // todo: 待优化
            if (!"system".equals(messageList.getFirst().getRole())) {
                messageList.addFirst(Message.system(model.getReasonerModel().getSystemMessage()));
            } else {
                messageList.getFirst().setContent(model.getGenerateModel().getSystemMessage());
            }

            DefaultRequest generateRequest = DefaultRequest.builder()
                    .model(model.getGenerateModel().getModelName())
                    .messages(messageList)
                    .build();

            generateClient.postSync(generateRequest, ChatResponse.class, listener);
            ChatResponse finalResponse = listener.onFinish();

            return ModelResponse.of(finalResponse);
        }
    }

    @Override
    public <R extends DefaultRequest> ModelResponse<ChatResponse> chat(R request) {
        boolean isStream = model.isStream();
        List<Message> messageList = new ArrayList<>(request.getMessages());
        request.setMessages(messageList);
        messageList = request.getMessages();
        if (!"system".equals(messageList.getFirst().getRole())) {
            messageList.addFirst(Message.system(model.getSystemMessage()));
        }
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
                List<Message> generatorMessageList = request.getMessages();
                String originContent = generatorMessageList.getLast().getContent();
                String newContent = reasoningContentBuffer + originContent;
                generatorMessageList.getLast().setContent(newContent);

                if (!"system".equals(generatorMessageList.getFirst().getRole())) {
                    generatorMessageList.addFirst(Message.system(model.getReasonerModel().getSystemMessage()));
                } else {
                    generatorMessageList.getFirst().setContent(model.getGenerateModel().getSystemMessage());
                }

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

            reasoningContent = "<think>\n" + reasoningContent + "</think>\n";

            List<Message> messages = request.getMessages();
            String originContent = messages.getLast().getContent();
            String newContent = reasoningContent + originContent;
            messages.getLast().setContent(newContent);

            if (!"system".equals(messages.getFirst().getRole())) {
                messages.addFirst(Message.system(model.getReasonerModel().getSystemMessage()));
            } else {
                messages.getFirst().setContent(model.getGenerateModel().getSystemMessage());
            }

            generateClient.postSync(request, ChatResponse.class, listener);
            ChatResponse finalResponse = listener.onFinish();

            return ModelResponse.of(finalResponse);
        }
    }
}
