package org.altegox.framework.client;

import org.altegox.common.utils.Json;
import org.altegox.framework.service.listener.ChatListener;
import org.altegox.framework.api.HttpClient;
import org.altegox.framework.api.LangModel;
import org.altegox.framework.api.request.DefaultRequest;
import org.altegox.framework.api.request.Message;
import org.altegox.framework.api.response.ChatResponse;
import org.altegox.framework.api.response.ModelResponse;
import org.altegox.framework.service.ChatService;
import org.altegox.common.log.Log;
import org.altegox.framework.toolcall.caller.Caller;
import org.altegox.framework.toolcall.caller.ToolCaller;

import java.util.List;

public abstract class ChatClient<T extends LangModel> implements ChatService<ChatResponse> {

    protected final T model;
    protected final HttpClient httpClient;

    protected ChatClient(T model) {
        this.model = model;
        this.httpClient = new HttpClient(model.getBaseUrl(), model.getApiKey());
    }

    @Override
    public String generate(String message) {
        DefaultRequest request = DefaultRequest.builder()
                .model(model.getModelName())
                .messages(List.of(Message.user(message)))
                .tools(model.getTools())
                .build();
        ChatListener<ChatResponse> listener = new ChatListener<>();
        httpClient.postSync(request, ChatResponse.class, listener);
        ChatResponse chatResponse = listener.onFinish();
        if ("tool_calls".equals(chatResponse.getChoices().getFirst().getFinishReason())
                && chatResponse.getChoices().getFirst().getMessage().getToolCalls() != null) {
            Caller<String> toolCaller = new ToolCaller<>();
            String toolName = chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getFunction().getName();
            String toolArgs = Json.toJson(chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getFunction().getArguments());

            String callResult = toolCaller.call(toolName, toolArgs);
            List<Message> messages = List.of(
                    Message.user(message),
                    chatResponse.getChoices().getFirst().getMessage(),
                    Message.tool(callResult, chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getId())
            );
            DefaultRequest toolCallRequest = DefaultRequest.builder()
                    .model(model.getModelName())
                    .messages(messages)
                    .build();
            httpClient.postSync(toolCallRequest, ChatResponse.class, listener);
            ChatResponse toolCallResponse = listener.onFinish();
            return toolCallResponse.getChoices().getFirst().getMessage().getContent();
        }
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
                .tools(model.getTools())
                .build();
        ChatListener<ChatResponse> listener = new ChatListener<>();
        // 启用了tool_call
        if (model.getTools() != null) {
            Caller<String> toolCaller = new ToolCaller<>();
            request.setStream(false);
            httpClient.postSync(request, ChatResponse.class, listener);
            ChatResponse chatResponse = listener.onFinish();
            if ("tool_calls".equals(chatResponse.getChoices().getFirst().getFinishReason())
                    && chatResponse.getChoices().getFirst().getMessage().getToolCalls() != null) {
                String toolName = chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getFunction().getName();
                String toolArgs = Json.toJson(chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getFunction().getArguments());

                String callResult = toolCaller.call(toolName, toolArgs);
                List<Message> toolCallMessages = List.of(
                        messages.getFirst(),
                        chatResponse.getChoices().getFirst().getMessage(),
                        Message.tool(callResult, chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getId())
                );

                DefaultRequest toolCallRequest = DefaultRequest.builder()
                        .model(model.getModelName())
                        .messages(toolCallMessages)
                        .build();

                ChatListener<ChatResponse> listener2 = new ChatListener<>();

                if (model.isStream()) {
                    return ModelResponse.of(httpClient.post(toolCallRequest, ChatResponse.class));
                } else {
                    httpClient.postSync(toolCallRequest, ChatResponse.class, listener2);
                    return ModelResponse.of(listener2.onFinish());
                }
            }
        }

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

        ChatListener<ChatResponse> listener = new ChatListener<>();
        // 启用了tool_call
        if (model.getTools() != null) {
            Caller<String> toolCaller = new ToolCaller<>();
            request.setStream(false);
            httpClient.postSync(request, ChatResponse.class, listener);
            ChatResponse chatResponse = listener.onFinish();
            if ("tool_calls".equals(chatResponse.getChoices().getFirst().getFinishReason())
                    && chatResponse.getChoices().getFirst().getMessage().getToolCalls() != null) {
                String toolName = chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getFunction().getName();
                String toolArgs = Json.toJson(chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getFunction().getArguments());

                String callResult = toolCaller.call(toolName, toolArgs);
                List<Message> toolCallMessages = List.of(
                        request.getMessages().getFirst(),
                        chatResponse.getChoices().getFirst().getMessage(),
                        Message.tool(callResult, chatResponse.getChoices().getFirst().getMessage().getToolCalls().getFirst().getId())
                );
                request.setMessages(toolCallMessages);
                request.setTools(null);
                ChatListener<ChatResponse> listener2 = new ChatListener<>();

                if (model.isStream()) {
                    return ModelResponse.of(httpClient.post(request, ChatResponse.class));
                } else {
                    httpClient.postSync(request, ChatResponse.class, listener2);
                    return ModelResponse.of(listener2.onFinish());
                }
            }
        }

        if (model.isStream()) {
            return ModelResponse.of(httpClient.post(request, ChatResponse.class));
        } else {
            httpClient.postSync(request, ChatResponse.class, listener);
            return ModelResponse.of(listener.onFinish());
        }
    }

}